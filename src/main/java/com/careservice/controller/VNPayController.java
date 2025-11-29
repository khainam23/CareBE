package com.careservice.controller;

import com.careservice.dto.ApiResponse;
import com.careservice.dto.payment.VNPayRequest;
import com.careservice.dto.payment.VNPayResponse;
import com.careservice.service.VNPayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/customer/payments/vnpay")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
public class VNPayController {
    
    private final VNPayService vnPayService;
    
    @PostMapping("/generate-url")
    public ResponseEntity<ApiResponse<VNPayResponse>> generatePaymentUrl(@Valid @RequestBody VNPayRequest request) {
        try {
            VNPayResponse response = vnPayService.generatePaymentUrl(request);
            return ResponseEntity.ok(ApiResponse.success("Payment URL generated successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/callback")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<String>> handleVNPayCallback(@RequestParam Map<String, String> params) {
        try {
            vnPayService.handleVNPayReturn(params);
            return ResponseEntity.ok(ApiResponse.success("Payment processed successfully", "OK"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/notify")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> handleVNPayNotify(@RequestParam Map<String, String> params) {
        try {
            vnPayService.handleVNPayReturn(params);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("FAILED");
        }
    }
}
