package com.careservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_rooms", indexes = {
    @Index(name = "idx_customer", columnList = "customerId"),
    @Index(name = "idx_caregiver", columnList = "caregiverId"),
    @Index(name = "idx_last_message", columnList = "lastMessageAt")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;
    
    @Column(nullable = false)
    private Long customerId;
    
    @Column(nullable = true)
    private Long caregiverId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ChatRoomStatus status = ChatRoomStatus.ACTIVE;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    private LocalDateTime lastMessageAt;
    
    public enum ChatRoomStatus {
        ACTIVE,
        ARCHIVED
    }
}
