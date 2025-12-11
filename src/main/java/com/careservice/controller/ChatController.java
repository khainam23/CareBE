package com.careservice.controller;

import com.careservice.dto.chat.ChatMessageDTO;
import com.careservice.dto.chat.SendMessageRequest;
import com.careservice.dto.chat.TypingIndicatorMessage;
import com.careservice.entity.ChatMessage.SenderType;
import com.careservice.security.UserPrincipal;
import com.careservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final org.springframework.data.redis.core.RedisTemplate<String, String> redisTemplate;
    
    private static final String TYPING_KEY_PREFIX = "typing:room:";
    
    @MessageMapping("/chat/send")
    public void sendMessage(@Payload SendMessageRequest request, 
                           @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            Long senderId = userPrincipal.getId();
            String roleName = userPrincipal.getRoleName();
            
            // Determine sender type based on role
            SenderType senderType = (roleName.contains("CUSTOMER")) ? SenderType.CUSTOMER : SenderType.CAREGIVER;
            
            // Send message
            ChatMessageDTO message = chatService.sendMessage(request, senderId, senderType);
            
            // Broadcast to chat room
            messagingTemplate.convertAndSend("/topic/chat/" + request.getChatRoomId(), message);
            
            log.info("Message sent to chat room {}", request.getChatRoomId());
            
        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage(), e);
            messagingTemplate.convertAndSendToUser(
                userPrincipal.getUsername(),
                "/queue/errors",
                "Failed to send message: " + e.getMessage()
            );
        }
    }
    
    @MessageMapping("/chat/typing")
    public void sendTypingIndicator(@Payload TypingIndicatorMessage message,
                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            message.setUserId(userPrincipal.getId());
            message.setUserName(userPrincipal.getFullName());
            
            // Store typing state in Redis
            String key = TYPING_KEY_PREFIX + message.getChatRoomId() + ":user:" + userPrincipal.getId();
            redisTemplate.opsForValue().set(key, "true", 5, java.util.concurrent.TimeUnit.SECONDS);
            
            // Broadcast typing indicator to chat room
            messagingTemplate.convertAndSend("/topic/chat/" + message.getChatRoomId() + "/typing", message);
            
        } catch (Exception e) {
            log.error("Error sending typing indicator: {}", e.getMessage(), e);
        }
    }
    
    @MessageMapping("/chat/read")
    public void markAsRead(@Payload Long chatRoomId,
                          @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            chatService.markMessagesAsRead(chatRoomId, userPrincipal.getId());
            
            // Broadcast read receipt
            messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId + "/read", userPrincipal.getId());
            
        } catch (Exception e) {
            log.error("Error marking messages as read: {}", e.getMessage(), e);
        }
    }
}
