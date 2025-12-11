package com.careservice.controller;

import com.careservice.dto.ApiResponse;
import com.careservice.dto.payment.VNPayRequest;
import com.careservice.dto.payment.VNPayResponse;
import com.careservice.service.VNPayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/customer/payments/vnpay")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
public class VNPayController {
    
    private final VNPayService vnPayService;
    
    @PostMapping("/generate-url")
    public ResponseEntity<ApiResponse<VNPayResponse>> generatePaymentUrl(
            @Valid @RequestBody VNPayRequest request,
            jakarta.servlet.http.HttpServletRequest httpRequest) {
        try {
            log.info("Generating VNPay URL for booking: {}", request.getBookingId());
            
            // Get client IP address
            String ipAddress = getClientIp(httpRequest);
            log.info("Client IP Address: {}", ipAddress);
            
            VNPayResponse response = vnPayService.generatePaymentUrl(request, ipAddress);
            log.info("VNPay URL generated successfully for transaction: {}", response.getTransactionId());
            return ResponseEntity.ok(ApiResponse.success("Tạo URL thanh toán VNPay thành công", response));
        } catch (RuntimeException e) {
            log.error("VNPay generation error for booking: {}", request.getBookingId(), e);
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Có lỗi xảy ra khi tạo thanh toán";
            return ResponseEntity.badRequest().body(ApiResponse.error(errorMessage));
        } catch (Exception e) {
            log.error("Unexpected error generating VNPay URL for booking: {}", request.getBookingId(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Có lỗi xảy ra trong quá trình xử lý. Vui lòng thử lại sau."));
        }
    }
    
    private String getClientIp(jakarta.servlet.http.HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        // If multiple IPs, take the first one
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        return ipAddress != null ? ipAddress : "127.0.0.1";
    }
    
    @GetMapping("/callback")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<String>> handleVNPayCallback(@RequestParam Map<String, String> params) {
        try {
            String txnRef = params.get("vnp_TxnRef");
            log.info("VNPay callback received for transaction: {}", txnRef);
            vnPayService.handleVNPayReturn(params);
            log.info("VNPay callback processed successfully for transaction: {}", txnRef);
            return ResponseEntity.ok(ApiResponse.success("Payment processed successfully", "OK"));
        } catch (Exception e) {
            log.error("Error processing VNPay callback", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/notify")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> handleVNPayNotify(@RequestParam Map<String, String> params) {
        try {
            String txnRef = params.get("vnp_TxnRef");
            log.info("VNPay notify received for transaction: {}", txnRef);
            vnPayService.handleVNPayReturn(params);
            log.info("VNPay notify processed successfully for transaction: {}", txnRef);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            log.error("Error processing VNPay notify for transaction: {}", params.get("vnp_TxnRef"), e);
            return ResponseEntity.badRequest().body("FAILED");
        }
    }
}
