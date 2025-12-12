package com.careservice.controller;

import com.careservice.dto.ApiResponse;
import com.careservice.dto.chat.ChatMessageDTO;
import com.careservice.dto.chat.ChatRoomDTO;
import com.careservice.dto.chat.SendMessageRequest;
import com.careservice.entity.ChatMessage.SenderType;
import com.careservice.security.UserPrincipal;
import com.careservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRestController {
    
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    
    @GetMapping("/rooms")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'CAREGIVER')")
    public ResponseEntity<ApiResponse<List<ChatRoomDTO>>> getChatRooms(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<ChatRoomDTO> chatRooms = chatService.getUserChatRooms(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Chat rooms retrieved successfully", chatRooms));
    }
    
    @GetMapping("/rooms/booking/{bookingId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'CAREGIVER')")
    public ResponseEntity<ApiResponse<ChatRoomDTO>> getChatRoomByBooking(
            @PathVariable Long bookingId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        try {
            ChatRoomDTO chatRoom = chatService.getChatRoomByBooking(bookingId, userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success("Chat room retrieved successfully", chatRoom));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Chat room not found")) {
                try {
                    System.out.println("Chat room not found, attempting to create for booking: " + bookingId);
                    chatService.createChatRoom(bookingId);
                    ChatRoomDTO chatRoom = chatService.getChatRoomByBooking(bookingId, userPrincipal.getId());
                    return ResponseEntity.ok(ApiResponse.success("Chat room created and retrieved successfully", chatRoom));
                } catch (Exception createEx) {
                    System.err.println("Failed to create chat room: " + createEx.getMessage());
                    createEx.printStackTrace();
                    return ResponseEntity.ok(ApiResponse.success("Chat room not available yet", null));
                }
            }
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/rooms/create-for-booking/{bookingId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'CAREGIVER')")
    public ResponseEntity<ApiResponse<ChatRoomDTO>> createChatRoomForBooking(
            @PathVariable Long bookingId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        try {
            System.out.println("Creating chat room for booking: " + bookingId);
            chatService.createChatRoom(bookingId);
            ChatRoomDTO chatRoom = chatService.getChatRoomByBooking(bookingId, userPrincipal.getId());
            System.out.println("Chat room created successfully, ID: " + chatRoom.getId());
            return ResponseEntity.ok(ApiResponse.success("Chat room created successfully", chatRoom));
        } catch (Exception e) {
            System.err.println("Failed to create chat room for booking " + bookingId + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to create chat room: " + e.getMessage()));
        }
    }
    
    @GetMapping("/rooms/{roomId}/messages")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'CAREGIVER')")
    public ResponseEntity<ApiResponse<Page<ChatMessageDTO>>> getMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Page<ChatMessageDTO> messages = chatService.getMessageHistory(roomId, userPrincipal.getId(), page, size);
        return ResponseEntity.ok(ApiResponse.success("Messages retrieved successfully", messages));
    }
    
    @PostMapping("/rooms/{roomId}/messages")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'CAREGIVER')")
    public ResponseEntity<ApiResponse<ChatMessageDTO>> sendMessage(
            @PathVariable Long roomId,
            @RequestBody SendMessageRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        try {
            // Set the chat room ID from path variable
            request.setChatRoomId(roomId);
            
            Long senderId = userPrincipal.getId();
            String roleName = userPrincipal.getRoleName();
            
            // Determine sender type based on role
            SenderType senderType = (roleName.contains("CUSTOMER")) ? SenderType.CUSTOMER : SenderType.CAREGIVER;
            
            // Send message via service
            ChatMessageDTO message = chatService.sendMessage(request, senderId, senderType);
            
            // Try to broadcast via WebSocket (will fail silently if no subscribers)
            try {
                messagingTemplate.convertAndSend("/topic/chat/" + roomId, message);
            } catch (Exception wsError) {
                // WebSocket broadcast failed, but message was saved - this is OK
                System.out.println("WebSocket broadcast failed (no active subscribers): " + wsError.getMessage());
            }
            
            return ResponseEntity.ok(ApiResponse.success("Message sent successfully", message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to send message: " + e.getMessage()));
        }
    }
    
    @PostMapping("/rooms/{roomId}/read")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'CAREGIVER')")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable Long roomId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        chatService.markMessagesAsRead(roomId, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Messages marked as read", null));
    }
    
    @GetMapping("/unread-count")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'CAREGIVER')")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        long unreadCount = chatService.getUnreadCount(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Unread count retrieved successfully", unreadCount));
    }
}
