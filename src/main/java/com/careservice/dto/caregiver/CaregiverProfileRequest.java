package com.careservice.dto.caregiver;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaregiverProfileRequest {
    
    private String bio;
    
    @NotBlank(message = "Skills are required")
    private String skills;
    
    private String experience;
    
    @NotBlank(message = "ID card number is required")
    private String idCardNumber;
    
    private String idCardUrl;
    
    private String certificateUrls;
    
    private String availableSchedule;
    
    private BigDecimal hourlyRate;
}
