package com.careservice.dto.caregiver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaregiverDTO {
    
    private Long id;
    private Long userId;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String bio;
    private String skills;
    private String experience;
    private Integer experienceYears;
    private String idCardNumber;
    private String idCardUrl;
    private String certificateUrls;
    private String verificationStatus;
    private String rejectionReason;
    private Boolean isAvailable;
    private String availableSchedule;
    private BigDecimal hourlyRate;
    private Double rating;
    private Integer totalReviews;
    private Integer completedBookings;
    private BigDecimal totalEarnings;
    private LocalDateTime verifiedAt;
    private LocalDateTime createdAt;
}
