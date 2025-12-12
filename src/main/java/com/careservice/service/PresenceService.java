package com.careservice.service;

import com.careservice.dto.chat.PresenceMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class PresenceService {
    
    // In-memory storage for presence (replaces Redis)
    private final Map<Long, PresenceMessage> presenceStore = new ConcurrentHashMap<>();
    
    public void setUserOnline(Long userId, String sessionId) {
        PresenceMessage presence = PresenceMessage.builder()
                .userId(userId)
                .isOnline(true)
                .lastSeen(LocalDateTime.now())
                .build();
        
        presenceStore.put(userId, presence);
        log.debug("User {} is now online with session {}", userId, sessionId);
    }
    
    public void setUserOffline(Long userId) {
        PresenceMessage presence = PresenceMessage.builder()
                .userId(userId)
                .isOnline(false)
                .lastSeen(LocalDateTime.now())
                .build();
        
        presenceStore.put(userId, presence);
        log.debug("User {} is now offline", userId);
    }
    
    public PresenceMessage getUserPresence(Long userId) {
        PresenceMessage presence = presenceStore.get(userId);
        
        if (presence != null) {
            return presence;
        }
        
        return PresenceMessage.builder()
                .userId(userId)
                .isOnline(false)
                .lastSeen(null)
                .build();
    }
    
    public boolean isUserOnline(Long userId) {
        PresenceMessage presence = getUserPresence(userId);
        return presence.isOnline();
    }
    
    public void refreshPresence(Long userId) {
        PresenceMessage presence = presenceStore.get(userId);
        if (presence != null && presence.isOnline()) {
            presence.setLastSeen(LocalDateTime.now());
            presenceStore.put(userId, presence);
        }
    }
}
