package com.careservice.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Set<String> roles;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String avatarUrl;
    private String avatarImage;
    private String imageSource;
    private CaregiverProfileDTO caregiverProfile;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CaregiverProfileDTO {
        private Long id;
        private String bio;
        private String skills;
        private String experience;
        private Integer experienceYears;
        private String avatarImage;
        private String imageSource;
        private String idCardNumber;
        private String idCardUrl;
        private String certificateUrls;
        private String verificationStatus;
        private Boolean isAvailable;
        private String availableSchedule;
        private Double hourlyRate;
        private Double rating;
        private Integer totalReviews;
        private Integer completedBookings;
    }
}
