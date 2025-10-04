package com.careservice.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    
    private Long id;
    private Long bookingId;
    private String bookingCode;
    private Long customerId;
    private String customerName;
    private Long caregiverId;
    private String caregiverName;
    private Integer rating;
    private String comment;
    private String caregiverResponse;
    private LocalDateTime respondedAt;
    private LocalDateTime createdAt;
}
