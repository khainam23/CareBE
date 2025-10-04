package com.careservice.controller;

import com.careservice.dto.ApiResponse;
import com.careservice.dto.admin.DashboardStatsDTO;
import com.careservice.dto.caregiver.CaregiverDTO;
import com.careservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private final AdminService adminService;
    
    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse<DashboardStatsDTO>> getDashboardStats() {
        try {
            DashboardStatsDTO stats = adminService.getDashboardStats();
            return ResponseEntity.ok(ApiResponse.success("Dashboard stats retrieved successfully", stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/caregivers/pending")
    public ResponseEntity<ApiResponse<List<CaregiverDTO>>> getPendingCaregivers() {
        try {
            List<CaregiverDTO> caregivers = adminService.getPendingCaregivers();
            return ResponseEntity.ok(ApiResponse.success("Pending caregivers retrieved successfully", caregivers));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/caregivers/{id}/approve")
    public ResponseEntity<ApiResponse<CaregiverDTO>> approveCaregiver(@PathVariable Long id) {
        try {
            CaregiverDTO caregiver = adminService.approveCaregiver(id);
            return ResponseEntity.ok(ApiResponse.success("Caregiver approved successfully", caregiver));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/caregivers/{id}/reject")
    public ResponseEntity<ApiResponse<CaregiverDTO>> rejectCaregiver(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String reason = request.get("reason");
            CaregiverDTO caregiver = adminService.rejectCaregiver(id, reason);
            return ResponseEntity.ok(ApiResponse.success("Caregiver rejected", caregiver));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/users/{id}/suspend")
    public ResponseEntity<ApiResponse<Void>> suspendUser(@PathVariable Long id) {
        try {
            adminService.suspendUser(id);
            return ResponseEntity.ok(ApiResponse.success("User suspended successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/users/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable Long id) {
        try {
            adminService.activateUser(id);
            return ResponseEntity.ok(ApiResponse.success("User activated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
