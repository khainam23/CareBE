package com.careservice.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerProfileRequest {
    @NotBlank(message = "Name is required")
    private String name;
    
    @Email(message = "Invalid email format")
    private String email;
    
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private String gender;
    private String notes;
    private String emergencyContact;
    private String emergencyPhone;
}
