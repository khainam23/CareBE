package com.careservice.dto.admin;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateServiceRequest {
    
    @NotBlank(message = "Service name is required")
    @Size(min = 3, max = 100, message = "Service name must be between 3 and 100 characters")
    private String name;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotBlank(message = "Category is required")
    private String category; // ELDERLY_CARE, CHILD_CARE, MEDICAL_CARE, COMPANION, HOUSEKEEPING, NURSING, REHABILITATION, OTHER
    
    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be greater than 0")
    private BigDecimal basePrice;
    
    @NotNull(message = "Duration is required")
    @Min(value = 15, message = "Duration must be at least 15 minutes")
    private Integer durationMinutes;
    
    private Boolean isActive = true;
}
