package com.careservice.dto.customer;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAddressRequest {
    
    @NotBlank(message = "Address is required")
    private String address;
    
    private String label;
    
    private Boolean isDefault = false;
}
