package com.careservice.dto.admin;

import jakarta.validation.constraints.NotNull;
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
public class CreateBookingRequest {
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    private Long caregiverId;
    
    @NotNull(message = "Service ID is required")
    private Long serviceId;
    
    @NotNull(message = "Scheduled start time is required")
    private LocalDateTime scheduledStartTime;
    
    @NotNull(message = "Scheduled end time is required")
    private LocalDateTime scheduledEndTime;
    
    @NotNull(message = "Total price is required")
    private BigDecimal totalPrice;
    
    private String customerNote;
    
    private String location;
}
