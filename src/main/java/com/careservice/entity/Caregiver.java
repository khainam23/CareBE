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
@Table(name = "caregivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Caregiver {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    @Column(columnDefinition = "TEXT")
    private String bio;
    
    @Column(columnDefinition = "TEXT")
    private String skills;
    
    @Column(columnDefinition = "TEXT")
    private String experience;
    
    private Integer experienceYears;
    
    private String idCardNumber;
    
    private String idCardUrl;
    
    private String avatarImage;
    
    private String imageSource;
    
    @Column(columnDefinition = "TEXT")
    private String certificateUrls;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;
    
    private String rejectionReason;
    
    @Column(nullable = false)
    private Boolean isAvailable = false;
    
    @Column(columnDefinition = "TEXT")
    private String availableSchedule;
    
    @Column(nullable = false)
    private BigDecimal hourlyRate = BigDecimal.ZERO;
    
    @Column(nullable = false)
    private Double rating = 0.0;
    
    @Column(nullable = false)
    private Integer totalReviews = 0;
    
    @Column(nullable = false)
    private Integer completedBookings = 0;
    
    @Column(nullable = false)
    private BigDecimal totalEarnings = BigDecimal.ZERO;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    private LocalDateTime verifiedAt;
    
    public enum VerificationStatus {
        PENDING, APPROVED, REJECTED, SUSPENDED
    }
}
