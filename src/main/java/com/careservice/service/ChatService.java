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
    
    @Transactional
    public ChatRoom createChatRoom(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        Long customerId = booking.getCustomer().getUser().getId();
        Long caregiverId = booking.getCaregiver() != null ? booking.getCaregiver().getUser().getId() : null;
        
        ChatRoom existingBookingRoom = chatRoomRepository.findByBookingId(bookingId).orElse(null);

        if (caregiverId != null) {
            // Check for existing conversation with this caregiver
            java.util.Optional<ChatRoom> historicalRoomOpt = chatRoomRepository.findByCustomerIdAndCaregiverId(customerId, caregiverId);
            
            if (historicalRoomOpt.isPresent()) {
                ChatRoom historicalRoom = historicalRoomOpt.get();
                
                // If the historical room is ALREADY the booking room, just return it
                if (existingBookingRoom != null && existingBookingRoom.getId().equals(historicalRoom.getId())) {
                     return historicalRoom;
                }

                // If we have a pending room for this booking that is different from historical room
                if (existingBookingRoom != null) {
                    // Delete the temporary pending room to free up the booking_id unique constraint
                    chatRoomRepository.delete(existingBookingRoom);
                    chatRoomRepository.flush();
                }
                
                // Link historical room to this new booking
                historicalRoom.setBooking(booking);
                log.info("Reusing existing chat room {} for booking {}", historicalRoom.getId(), bookingId);
                return chatRoomRepository.save(historicalRoom);
            }
        }

        // No historical room with this caregiver.
        if (existingBookingRoom != null) {
            // Just update existing room (e.g. assigning caregiver for first time)
            existingBookingRoom.setCaregiverId(caregiverId);
            return chatRoomRepository.save(existingBookingRoom);
        }
        
        ChatRoom chatRoom = ChatRoom.builder()
                .booking(booking)
                .customerId(customerId)
                .caregiverId(caregiverId)
                .status(ChatRoom.ChatRoomStatus.ACTIVE)
                .build();
        
        chatRoom = chatRoomRepository.save(chatRoom);
        log.info("Created chat room {} for booking {} (customer user: {}, caregiver user: {})", 
                chatRoom.getId(), bookingId, customerId, caregiverId);
        
        return chatRoom;
    }
    
    @Transactional
    public ChatMessageDTO sendMessage(SendMessageRequest request, Long senderId, SenderType senderType) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId())
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        
        // Validate sender is participant
        if (!isParticipant(chatRoom, senderId, senderType)) {
            throw new RuntimeException("User is not a participant of this chat room");
        }
        
        // Validate booking status - allow PENDING for customers only
        Booking booking = chatRoom.getBooking();
        if (senderType == SenderType.CAREGIVER) {
            if (booking.getStatus() != Booking.BookingStatus.CONFIRMED && 
                booking.getStatus() != Booking.BookingStatus.IN_PROGRESS &&
                booking.getStatus() != Booking.BookingStatus.ASSIGNED) {
                throw new RuntimeException("Cannot send messages for bookings that are not assigned, confirmed or in progress");
            }
        } else {
            if (booking.getStatus() == Booking.BookingStatus.CANCELLED || 
                booking.getStatus() == Booking.BookingStatus.REJECTED) {
                throw new RuntimeException("Cannot send messages for cancelled or rejected bookings");
            }
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
        // First try to find by booking ID
        ChatRoom chatRoom = chatRoomRepository.findByBookingId(bookingId).orElse(null);
        
        // If not found by booking ID (might be linked to another booking now), try by participants
        if (chatRoom == null) {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));
            
            Long customerId = booking.getCustomer().getUser().getId();
            Long caregiverId = booking.getCaregiver() != null ? booking.getCaregiver().getUser().getId() : null;
            
            if (caregiverId != null) {
                chatRoom = chatRoomRepository.findByCustomerIdAndCaregiverId(customerId, caregiverId)
                        .orElseThrow(() -> new RuntimeException("Chat room not found for this booking context"));
            } else {
                throw new RuntimeException("Chat room not found");
            }
        }
        
        // Validate user is participant (customer or caregiver if assigned)
        boolean isCustomer = chatRoom.getCustomerId().equals(userId);
        boolean isCaregiver = chatRoom.getCaregiverId() != null && chatRoom.getCaregiverId().equals(userId);
        
        if (!isCustomer && !isCaregiver) {
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
            return chatRoom.getCaregiverId() != null && chatRoom.getCaregiverId().equals(userId);
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
        User caregiver = room.getCaregiverId() != null ? userRepository.findById(room.getCaregiverId()).orElse(null) : null;
        
        ChatMessage lastMessage = chatMessageRepository.findLastMessageByChatRoomId(room.getId());
        long unreadCount = chatMessageRepository.countUnreadMessages(room.getId(), currentUserId);
        
        Booking booking = room.getBooking();
        
        return ChatRoomDTO.builder()
                .id(room.getId())
                .bookingId(room.getBooking().getId())
                .customerId(room.getCustomerId())
                .customerName(customer != null ? customer.getFullName() : "Unknown")
                .caregiverId(room.getCaregiverId())
                .caregiverName(caregiver != null ? caregiver.getFullName() : "Awaiting caregiver assignment")
                .status(room.getStatus())
                .createdAt(room.getCreatedAt())
                .lastMessageAt(room.getLastMessageAt())
                .lastMessagePreview(lastMessage != null ? lastMessage.getContent() : null)
                .unreadCount(unreadCount)
                .serviceName(booking.getService().getName())
                .scheduledStartTime(booking.getScheduledStartTime())
                .scheduledEndTime(booking.getScheduledEndTime())
                .location(booking.getLocation())
                .bookingStatus(booking.getStatus().name())
                .build();
    }
    
}
