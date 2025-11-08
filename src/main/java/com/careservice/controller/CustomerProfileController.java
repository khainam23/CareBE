package com.careservice.controller;

import com.careservice.dto.ApiResponse;
import com.careservice.dto.customer.CustomerProfileDTO;
import com.careservice.dto.customer.UpdateCustomerProfileRequest;
import com.careservice.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
public class CustomerProfileController {
    
    private final CustomerService customerService;
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CustomerProfileDTO>> getMyProfile() {
        try {
            CustomerProfileDTO profile = customerService.getCustomerProfile();
            return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", profile));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to retrieve profile: " + e.getMessage()));
        }
    }
    
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<CustomerProfileDTO>> updateMyProfile(
            @Valid @RequestBody UpdateCustomerProfileRequest request) {
        try {
            CustomerProfileDTO profile = customerService.updateCustomerProfile(request);
            return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", profile));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update profile: " + e.getMessage()));
        }
    }
}
