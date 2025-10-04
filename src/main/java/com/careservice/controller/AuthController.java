package com.careservice.controller;

import com.careservice.dto.ApiResponse;
import com.careservice.dto.auth.*;
import com.careservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * Đăng ký tài khoản Khách hàng
     * Endpoint: POST /api/auth/register/customer
     */
    @PostMapping("/register/customer")
    public ResponseEntity<ApiResponse<AuthResponse>> registerCustomer(@Valid @RequestBody CustomerRegisterRequest request) {
        try {
            AuthResponse response = authService.registerCustomer(request);
            return ResponseEntity.ok(ApiResponse.success(
                "Đăng ký thành công! Bạn có thể đặt dịch vụ ngay bây giờ.", 
                response
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Đăng ký tài khoản Chuyên viên chăm sóc
     * Endpoint: POST /api/auth/register/caregiver
     */
    @PostMapping("/register/caregiver")
    public ResponseEntity<ApiResponse<AuthResponse>> registerCaregiver(@Valid @RequestBody CaregiverRegisterRequest request) {
        try {
            AuthResponse response = authService.registerCaregiver(request);
            return ResponseEntity.ok(ApiResponse.success(
                "Đăng ký thành công! Hồ sơ của bạn đang được xem xét. Chúng tôi sẽ liên hệ sớm.", 
                response
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Đăng ký chung (deprecated - sử dụng endpoint cụ thể thay thế)
     * Endpoint: POST /api/auth/register
     */
    @Deprecated
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(ApiResponse.success("Đăng ký thành công", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Đăng nhập
     * Endpoint: POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
