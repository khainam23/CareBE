package com.careservice.controller;

import com.careservice.service.VNPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/api/vnpay-test")
@RequiredArgsConstructor
@Slf4j
public class VNPayTestController {
    
    private final VNPayService vnPayService;
    
    @GetMapping("/verify-config")
    public Map<String, Object> verifyConfig() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String testData = "key1=value1&key2=value2&key3=value3";
            String hashSecret = "QB4CPXIK6O6WTTXV0ZDBO1TKYJ83XLXR";
            
            String signature = vnPayService.hmacSHA512(hashSecret, testData);
            
            response.put("success", true);
            response.put("message", "VNPay configuration verified");
            response.put("test_data", testData);
            response.put("hash_secret_length", hashSecret.length());
            response.put("generated_signature", signature);
            response.put("notes", "If signature is consistent, HMAC is working correctly");
            
            log.info("VNPay signature test completed successfully");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            log.error("VNPay configuration test failed", e);
        }
        
        return response;
    }
    
    @GetMapping("/test-signature")
    public Map<String, Object> testSignature() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String hashSecret = "QB4CPXIK6O6WTTXV0ZDBO1TKYJ83XLXR";
            
            // Simulate VNPay parameters (in sorted order)
            Map<String, String> testParams = new TreeMap<>();
            testParams.put("vnp_Amount", "100000");
            testParams.put("vnp_Command", "pay");
            testParams.put("vnp_CreateDate", "20251208100000");
            testParams.put("vnp_CurrCode", "VND");
            testParams.put("vnp_ExpireDate", "20251208101500");
            testParams.put("vnp_Locale", "vn");
            testParams.put("vnp_OrderInfo", "Test Order");
            testParams.put("vnp_OrderType", "other");
            testParams.put("vnp_ReturnUrl", "http://localhost:3000/payment-return");
            testParams.put("vnp_TmnCode", "B0HI2VGU");
            testParams.put("vnp_TxnRef", "TEST20251208100000");
            testParams.put("vnp_Version", "2.1.0");
            testParams.put("vnp_NotifyUrl", "http://localhost:8080/api/customer/payments/vnpay/notify");
            
            // Build query string
            StringBuilder queryBuilder = new StringBuilder();
            testParams.forEach((key, value) -> {
                if (queryBuilder.length() > 0) {
                    queryBuilder.append("&");
                }
                try {
                    queryBuilder.append(key).append("=").append(java.net.URLEncoder.encode(value, "UTF-8"));
                } catch (Exception e) {
                    log.error("Encoding error", e);
                }
            });
            
            String queryString = queryBuilder.toString();
            String signature = vnPayService.hmacSHA512(hashSecret, queryString);
            
            response.put("success", true);
            response.put("test_params", testParams);
            response.put("query_string", queryString.substring(0, Math.min(100, queryString.length())) + "...");
            response.put("generated_signature", signature);
            response.put("message", "Please verify this signature with VNPay docs or test gateway");
            
            log.info("Test signature: {}", signature);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            log.error("Signature test failed", e);
        }
        
        return response;
    }
    
    @GetMapping("/validate-url")
    public Map<String, Object> validateUrl() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String hashSecret = "QB4CPXIK6O6WTTXV0ZDBO1TKYJ83XLXR";
            String apiUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
            
            // Create test parameters matching actual booking
            Map<String, String> vnpParams = new TreeMap<>();
            vnpParams.put("vnp_Version", "2.1.0");
            vnpParams.put("vnp_Command", "pay");
            vnpParams.put("vnp_TmnCode", "B0HI2VGU");
            vnpParams.put("vnp_Locale", "vn");
            vnpParams.put("vnp_CurrCode", "VND");
            vnpParams.put("vnp_TxnRef", "VNP" + System.currentTimeMillis());
            vnpParams.put("vnp_OrderInfo", "Booking payment BK12345678");  // No special chars
            vnpParams.put("vnp_OrderType", "other");
            vnpParams.put("vnp_Amount", "10000000");  // 100,000 VND
            vnpParams.put("vnp_ReturnUrl", "http://localhost:3000/payment-return");
            vnpParams.put("vnp_NotifyUrl", "http://localhost:8080/api/customer/payments/vnpay/notify");
            vnpParams.put("vnp_CreateDate", "20251209120000");
            vnpParams.put("vnp_ExpireDate", "20251209121500");
            
            // Build query string
            StringBuilder queryBuilder = new StringBuilder();
            vnpParams.forEach((key, value) -> {
                if (queryBuilder.length() > 0) {
                    queryBuilder.append("&");
                }
                try {
                    queryBuilder.append(key).append("=").append(java.net.URLEncoder.encode(value, "UTF-8"));
                } catch (Exception e) {
                    log.error("Encoding error", e);
                }
            });
            
            String queryString = queryBuilder.toString();
            String vnpSecureHash = vnPayService.hmacSHA512(hashSecret, queryString);
            String paymentUrl = apiUrl + "?" + queryString + "&vnp_SecureHash=" + vnpSecureHash;
            
            response.put("success", true);
            response.put("parameters", vnpParams);
            response.put("query_string_length", queryString.length());
            response.put("secure_hash", vnpSecureHash);
            response.put("payment_url", paymentUrl);
            response.put("url_length", paymentUrl.length());
            response.put("message", "Test this URL in browser to see VNPay response");
            
            log.info("=== VNPay URL Validation ===");
            log.info("Query String: {}", queryString);
            log.info("Secure Hash: {}", vnpSecureHash);
            log.info("Payment URL: {}", paymentUrl);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            log.error("URL validation failed", e);
        }
        
        return response;
    }
}
