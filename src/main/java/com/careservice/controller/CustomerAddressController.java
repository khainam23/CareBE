package com.careservice.controller;

import com.careservice.dto.ApiResponse;
import com.careservice.dto.customer.CreateAddressRequest;
import com.careservice.dto.customer.CustomerAddressDTO;
import com.careservice.service.CustomerAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/addresses")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
public class CustomerAddressController {
    
    private final CustomerAddressService addressService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerAddressDTO>>> getMyAddresses() {
        try {
            List<CustomerAddressDTO> addresses = addressService.getMyAddresses();
            return ResponseEntity.ok(ApiResponse.success("Addresses retrieved successfully", addresses));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to retrieve addresses: " + e.getMessage()));
        }
    }
    
    @GetMapping("/default")
    public ResponseEntity<ApiResponse<CustomerAddressDTO>> getDefaultAddress() {
        try {
            CustomerAddressDTO address = addressService.getDefaultAddress();
            return ResponseEntity.ok(ApiResponse.success("Default address retrieved successfully", address));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to retrieve default address: " + e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<CustomerAddressDTO>> createAddress(
            @Valid @RequestBody CreateAddressRequest request) {
        try {
            CustomerAddressDTO address = addressService.createAddress(request);
            return ResponseEntity.ok(ApiResponse.success("Address created successfully", address));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create address: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{addressId}/default")
    public ResponseEntity<ApiResponse<CustomerAddressDTO>> setDefaultAddress(
            @PathVariable Long addressId) {
        try {
            CustomerAddressDTO address = addressService.setDefaultAddress(addressId);
            return ResponseEntity.ok(ApiResponse.success("Default address updated successfully", address));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update default address: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable Long addressId) {
        try {
            addressService.deleteAddress(addressId);
            return ResponseEntity.ok(ApiResponse.success("Address deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete address: " + e.getMessage()));
        }
    }
}
