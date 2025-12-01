package com.careservice.controller;

import com.careservice.dto.ApiResponse;
import com.careservice.dto.chat.ChatMessageDTO;
import com.careservice.dto.chat.ChatRoomDTO;
import com.careservice.security.UserPrincipal;
import com.careservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRestController {
    
    private final ChatService chatService;
    
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
        
        ChatRoomDTO chatRoom = chatService.getChatRoomByBooking(bookingId, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Chat room retrieved successfully", chatRoom));
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
