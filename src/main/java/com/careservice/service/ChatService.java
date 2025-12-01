package com.careservice.service;

import com.careservice.dto.chat.ChatMessageDTO;
import com.careservice.dto.chat.ChatRoomDTO;
import com.careservice.dto.chat.SendMessageRequest;
import com.careservice.entity.*;
import com.careservice.entity.ChatMessage.MessageStatus;
import com.careservice.entity.ChatMessage.SenderType;
import com.careservice.repository.BookingRepository;
import com.careservice.repository.ChatMessageRepository;
import com.careservice.repository.ChatRoomRepository;
import com.careservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final org.springframework.data.redis.core.RedisTemplate<String, String> redisTemplate;
    
    private static final int MAX_MESSAGES_PER_MINUTE = 10;
    private static final String RATE_LIMIT_KEY_PREFIX = "rate_limit:chat:";
    
    @Transactional
    public ChatRoom createChatRoom(Long bookingId) {
        if (chatRoomRepository.existsByBookingId(bookingId)) {
            return chatRoomRepository.findByBookingId(bookingId)
                    .orElseThrow(() -> new RuntimeException("Chat room exists but cannot be found"));
        }
        
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        ChatRoom chatRoom = ChatRoom.builder()
                .booking(booking)
                .customerId(booking.getCustomer().getId())
                .caregiverId(booking.getCaregiver().getId())
                .status(ChatRoom.ChatRoomStatus.ACTIVE)
                .build();
        
        chatRoom = chatRoomRepository.save(chatRoom);
        log.info("Created chat room {} for booking {}", chatRoom.getId(), bookingId);
        
        return chatRoom;
    }
    
    @Transactional
    public ChatMessageDTO sendMessage(SendMessageRequest request, Long senderId, SenderType senderType) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId())
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        
        // Check rate limit
        checkRateLimit(senderId);
        
        // Validate sender is participant
        if (!isParticipant(chatRoom, senderId, senderType)) {
            throw new RuntimeException("User is not a participant of this chat room");
        }
        
        // Validate booking status
        Booking booking = chatRoom.getBooking();
        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED && 
            booking.getStatus() != Booking.BookingStatus.IN_PROGRESS) {
            throw new RuntimeException("Cannot send messages for bookings that are not confirmed or in progress");
        }
        
        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoom)
                .senderId(senderId)
                .senderType(senderType)
                .content(request.getContent())
                .status(MessageStatus.SENT)
                .build();
        
        message = chatMessageRepository.save(message);
        
        // Update last message timestamp
        chatRoom.setLastMessageAt(message.getCreatedAt());
        chatRoomRepository.save(chatRoom);
        
        log.info("Message {} sent in chat room {} by user {}", message.getId(), chatRoom.getId(), senderId);
        
        return convertToDTO(message);
    }
    
    @Transactional(readOnly = true)
    public Page<ChatMessageDTO> getMessageHistory(Long chatRoomId, Long userId, int page, int size) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        
        // Validate user is participant
        if (chatRoom.getCustomerId().equals(userId) || chatRoom.getCaregiverId().equals(userId)) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<ChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderByCreatedAtDesc(chatRoomId, pageable);
            
            return messages.map(this::convertToDTO);
        }
        
        throw new RuntimeException("User is not a participant of this chat room");
    }
    
    @Transactional(readOnly = true)
    public Page<ChatMessageDTO> getAdminMessageHistory(Long chatRoomId, int page, int size) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderByCreatedAtDesc(chatRoomId, pageable);
        
        return messages.map(this::convertToDTO);
    }
    
    @Transactional
    public void markMessagesAsRead(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        
        // Validate user is participant
        if (!chatRoom.getCustomerId().equals(userId) && !chatRoom.getCaregiverId().equals(userId)) {
            throw new RuntimeException("User is not a participant of this chat room");
        }
        
        int updatedCount = chatMessageRepository.markMessagesAsRead(chatRoomId, userId, MessageStatus.READ);
        log.debug("Marked {} messages as read in chat room {} for user {}", updatedCount, chatRoomId, userId);
    }
    
    @Transactional(readOnly = true)
    public List<ChatRoomDTO> getUserChatRooms(Long userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByUserId(userId);
        
        return chatRooms.stream()
                .map(room -> convertToRoomDTO(room, userId))
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ChatRoomDTO getChatRoomByBooking(Long bookingId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Chat room not found for this booking"));
        
        // Validate user is participant
        if (!chatRoom.getCustomerId().equals(userId) && !chatRoom.getCaregiverId().equals(userId)) {
            throw new RuntimeException("User is not a participant of this chat room");
        }
        
        return convertToRoomDTO(chatRoom, userId);
    }
    
    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByUserId(userId);
        
        return chatRooms.stream()
                .mapToLong(room -> chatMessageRepository.countUnreadMessages(room.getId(), userId))
                .sum();
    }
    
    private boolean isParticipant(ChatRoom chatRoom, Long userId, SenderType senderType) {
        if (senderType == SenderType.CUSTOMER) {
            return chatRoom.getCustomerId().equals(userId);
        } else {
            return chatRoom.getCaregiverId().equals(userId);
        }
    }
    
    private ChatMessageDTO convertToDTO(ChatMessage message) {
        User sender = userRepository.findById(message.getSenderId())
                .orElse(null);
        
        return ChatMessageDTO.builder()
                .id(message.getId())
                .chatRoomId(message.getChatRoom().getId())
                .senderId(message.getSenderId())
                .senderName(sender != null ? sender.getFullName() : "Unknown")
                .senderType(message.getSenderType())
                .content(message.getContent())
                .status(message.getStatus())
                .createdAt(message.getCreatedAt())
                .deliveredAt(message.getDeliveredAt())
                .readAt(message.getReadAt())
                .build();
    }
    
    private ChatRoomDTO convertToRoomDTO(ChatRoom room, Long currentUserId) {
        User customer = userRepository.findById(room.getCustomerId()).orElse(null);
        User caregiver = userRepository.findById(room.getCaregiverId()).orElse(null);
        
        ChatMessage lastMessage = chatMessageRepository.findLastMessageByChatRoomId(room.getId());
        long unreadCount = chatMessageRepository.countUnreadMessages(room.getId(), currentUserId);
        
        return ChatRoomDTO.builder()
                .id(room.getId())
                .bookingId(room.getBooking().getId())
                .customerId(room.getCustomerId())
                .customerName(customer != null ? customer.getFullName() : "Unknown")
                .caregiverId(room.getCaregiverId())
                .caregiverName(caregiver != null ? caregiver.getFullName() : "Unknown")
                .status(room.getStatus())
                .createdAt(room.getCreatedAt())
                .lastMessageAt(room.getLastMessageAt())
                .lastMessagePreview(lastMessage != null ? lastMessage.getContent() : null)
                .unreadCount(unreadCount)
                .build();
    }
    
    private void checkRateLimit(Long userId) {
        String key = RATE_LIMIT_KEY_PREFIX + userId;
        String countStr = redisTemplate.opsForValue().get(key);
        int count = countStr != null ? Integer.parseInt(countStr) : 0;
        
        if (count >= MAX_MESSAGES_PER_MINUTE) {
            throw new RuntimeException("Rate limit exceeded. Please wait a moment before sending more messages.");
        }
        
        redisTemplate.opsForValue().increment(key);
        if (count == 0) {
            redisTemplate.expire(key, 1, java.util.concurrent.TimeUnit.MINUTES);
        }
    }
}
