package com.careservice.config;

import com.careservice.dto.chat.PresenceMessage;
import com.careservice.security.UserPrincipal;
import com.careservice.service.PresenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    
    private final PresenceService presenceService;
    private final SimpMessagingTemplate messagingTemplate;
    
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = headerAccessor.getUser();
        
        if (user instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) user;
            Long userId = userPrincipal.getId();
            String sessionId = headerAccessor.getSessionId();
            
            presenceService.setUserOnline(userId, sessionId);
            
            // Broadcast presence update
            PresenceMessage presenceMessage = PresenceMessage.builder()
                    .userId(userId)
                    .isOnline(true)
                    .build();
            
            messagingTemplate.convertAndSend("/topic/presence/" + userId, presenceMessage);
            
            log.info("User {} connected with session {}", userId, sessionId);
        }
    }
    
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = headerAccessor.getUser();
        
        if (user instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) user;
            Long userId = userPrincipal.getId();
            
            presenceService.setUserOffline(userId);
            
            // Broadcast presence update
            PresenceMessage presenceMessage = presenceService.getUserPresence(userId);
            messagingTemplate.convertAndSend("/topic/presence/" + userId, presenceMessage);
            
            log.info("User {} disconnected", userId);
        }
    }
}
