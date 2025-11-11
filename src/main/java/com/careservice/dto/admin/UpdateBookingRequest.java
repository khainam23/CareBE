package com.careservice.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingRequest {
    
    private Long caregiverId;
    
    private LocalDateTime scheduledStartTime;
    
    private LocalDateTime scheduledEndTime;
    
    private BigDecimal totalPrice;
    
    private String status; // PENDING, CONFIRMED, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED, REJECTED
    
    private String customerNote;
    
    private String caregiverNote;
    
    private String location;
    
    private String cancellationReason;
}
