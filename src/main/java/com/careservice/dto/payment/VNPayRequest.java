package com.careservice.dto.payment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VNPayRequest {
    
    @NotNull(message = "Booking ID is required")
    private Long bookingId;
    
    private String notes;
}
