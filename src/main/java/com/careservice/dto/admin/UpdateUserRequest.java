package com.careservice.dto.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    
    @Email(message = "Email must be valid")
    private String email;
    
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    
    private String phoneNumber;
    
    private String address;
    
    private String role; // ROLE_CUSTOMER, ROLE_CAREGIVER, ROLE_SUPPORT, ROLE_ADMIN
    
    private String status; // ACTIVE, SUSPENDED, PENDING_APPROVAL, REJECTED
}
