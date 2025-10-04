package com.careservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;
    
    @Column(nullable = false)
    private Boolean isRead = false;
    
    private String relatedEntityType;
    
    private Long relatedEntityId;
    
    private LocalDateTime readAt;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    public enum NotificationType {
        BOOKING_CONFIRMATION,
        BOOKING_REMINDER,
        BOOKING_CANCELLED,
        BOOKING_COMPLETED,
        PAYMENT_SUCCESS,
        PAYMENT_FAILED,
        NEW_BOOKING_REQUEST,
        CAREGIVER_ASSIGNED,
        REVIEW_RECEIVED,
        ACCOUNT_VERIFIED,
        ACCOUNT_REJECTED,
        SUPPORT_TICKET_UPDATE,
        SYSTEM_NOTIFICATION
    }
}
