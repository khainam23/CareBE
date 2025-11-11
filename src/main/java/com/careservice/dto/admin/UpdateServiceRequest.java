package com.careservice.dto.admin;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateServiceRequest {
    
    @Size(min = 3, max = 100, message = "Service name must be between 3 and 100 characters")
    private String name;
    
    private String description;
    
    private String category; // ELDERLY_CARE, CHILD_CARE, MEDICAL_CARE, COMPANION, HOUSEKEEPING, NURSING, REHABILITATION, OTHER
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be greater than 0")
    private BigDecimal basePrice;
    
    @Min(value = 15, message = "Duration must be at least 15 minutes")
    private Integer durationMinutes;
    
    private Boolean isActive;
}
