package com.careservice.repository;

import com.careservice.entity.ChatMessage;
import com.careservice.entity.ChatMessage.MessageStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    Page<ChatMessage> findByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId, Pageable pageable);
    
    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.chatRoom.id = :chatRoomId AND m.senderId != :userId AND m.status != 'READ'")
    long countUnreadMessages(@Param("chatRoomId") Long chatRoomId, @Param("userId") Long userId);
    
    @Modifying
    @Query("UPDATE ChatMessage m SET m.status = :status, m.readAt = CURRENT_TIMESTAMP WHERE m.chatRoom.id = :chatRoomId AND m.senderId != :userId AND m.status != 'READ'")
    int markMessagesAsRead(@Param("chatRoomId") Long chatRoomId, @Param("userId") Long userId, @Param("status") MessageStatus status);
    
    @Query("SELECT m FROM ChatMessage m WHERE m.chatRoom.id = :chatRoomId ORDER BY m.createdAt DESC LIMIT 1")
    ChatMessage findLastMessageByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
