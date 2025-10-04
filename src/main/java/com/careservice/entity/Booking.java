package com.careservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String bookingCode;
    
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @ManyToOne
    @JoinColumn(name = "caregiver_id")
    private Caregiver caregiver;
    
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;
    
    @Column(nullable = false)
    private LocalDateTime scheduledStartTime;
    
    @Column(nullable = false)
    private LocalDateTime scheduledEndTime;
    
    private LocalDateTime actualStartTime;
    
    private LocalDateTime actualEndTime;
    
    @Column(nullable = false)
    private BigDecimal totalPrice;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;
    
    @Column(columnDefinition = "TEXT")
    private String customerNote;
    
    @Column(columnDefinition = "TEXT")
    private String caregiverNote;
    
    private String location;
    
    @Column(columnDefinition = "TEXT")
    private String cancellationReason;
    
    private LocalDateTime cancelledAt;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        ASSIGNED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        REJECTED
    }
}
