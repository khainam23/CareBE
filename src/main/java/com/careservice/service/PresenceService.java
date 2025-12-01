package com.careservice.service;

import com.careservice.dto.chat.PresenceMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PresenceService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PRESENCE_KEY_PREFIX = "presence:user:";
    private static final long PRESENCE_TTL_MINUTES = 5;
    
    public void setUserOnline(Long userId, String sessionId) {
        String key = PRESENCE_KEY_PREFIX + userId;
        PresenceMessage presence = PresenceMessage.builder()
                .userId(userId)
                .isOnline(true)
                .lastSeen(LocalDateTime.now())
                .build();
        
        redisTemplate.opsForValue().set(key, presence, PRESENCE_TTL_MINUTES, TimeUnit.MINUTES);
        log.debug("User {} is now online with session {}", userId, sessionId);
    }
    
    public void setUserOffline(Long userId) {
        String key = PRESENCE_KEY_PREFIX + userId;
        PresenceMessage presence = PresenceMessage.builder()
                .userId(userId)
                .isOnline(false)
                .lastSeen(LocalDateTime.now())
                .build();
        
        redisTemplate.opsForValue().set(key, presence, 24, TimeUnit.HOURS);
        log.debug("User {} is now offline", userId);
    }
    
    public PresenceMessage getUserPresence(Long userId) {
        String key = PRESENCE_KEY_PREFIX + userId;
        Object presence = redisTemplate.opsForValue().get(key);
        
        if (presence instanceof PresenceMessage) {
            return (PresenceMessage) presence;
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
        String key = PRESENCE_KEY_PREFIX + userId;
        Boolean hasKey = redisTemplate.hasKey(key);
        
        if (Boolean.TRUE.equals(hasKey)) {
            redisTemplate.expire(key, PRESENCE_TTL_MINUTES, TimeUnit.MINUTES);
        }
    }
}
