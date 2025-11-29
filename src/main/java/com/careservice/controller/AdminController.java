package com.careservice.controller;

import com.careservice.dto.ApiResponse;
import com.careservice.dto.admin.*;
import com.careservice.dto.booking.BookingDTO;
import com.careservice.dto.caregiver.CaregiverDTO;
import com.careservice.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    
    // ==================== User Management Endpoints ====================
    
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<UserDTO> users = adminService.getAllUsers(pageable);
            return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        try {
            UserDTO user = adminService.getUserById(id);
            return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            UserDTO user = adminService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User created successfully", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        try {
            UserDTO user = adminService.updateUser(id, request);
            return ResponseEntity.ok(ApiResponse.success("User updated successfully", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        try {
            adminService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/users/{id}/avatar")
    public ResponseEntity<ApiResponse<UserDTO>> uploadUserAvatar(
            @PathVariable Long id,
            @RequestParam("avatar") MultipartFile file,
            @RequestParam(value = "imageSource", defaultValue = "url") String imageSource) {
        try {
            UserDTO user = adminService.uploadUserAvatar(id, file, imageSource);
            return ResponseEntity.ok(ApiResponse.success("Avatar uploaded successfully", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== Caregiver Management Endpoints ====================
    
    @GetMapping("/caregivers")
    public ResponseEntity<ApiResponse<Page<CaregiverDTO>>> getAllCaregivers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<CaregiverDTO> caregivers = adminService.getAllCaregivers(pageable);
            return ResponseEntity.ok(ApiResponse.success("Caregivers retrieved successfully", caregivers));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/caregivers/{id}")
    public ResponseEntity<ApiResponse<CaregiverDTO>> getCaregiverById(@PathVariable Long id) {
        try {
            CaregiverDTO caregiver = adminService.getCaregiverById(id);
            return ResponseEntity.ok(ApiResponse.success("Caregiver retrieved successfully", caregiver));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/caregivers")
    public ResponseEntity<ApiResponse<CaregiverDTO>> createCaregiver(@Valid @RequestBody CreateUserRequest request) {
        try {
            CaregiverDTO caregiver = adminService.createCaregiver(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Caregiver created successfully", caregiver));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/caregivers/{id}")
    public ResponseEntity<ApiResponse<CaregiverDTO>> updateCaregiver(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        try {
            CaregiverDTO caregiver = adminService.updateCaregiver(id, request);
            return ResponseEntity.ok(ApiResponse.success("Caregiver updated successfully", caregiver));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/caregivers/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCaregiver(@PathVariable Long id) {
        try {
            adminService.deleteCaregiver(id);
            return ResponseEntity.ok(ApiResponse.success("Caregiver deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== Booking Management Endpoints ====================
    
    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<Page<BookingDTO>>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<BookingDTO> bookings = adminService.getAllBookings(pageable);
            return ResponseEntity.ok(ApiResponse.success("Bookings retrieved successfully", bookings));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/bookings/{id}")
    public ResponseEntity<ApiResponse<BookingDTO>> getBookingById(@PathVariable Long id) {
        try {
            BookingDTO booking = adminService.getBookingById(id);
            return ResponseEntity.ok(ApiResponse.success("Booking retrieved successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/bookings")
    public ResponseEntity<ApiResponse<BookingDTO>> createBooking(@Valid @RequestBody CreateBookingRequest request) {
        try {
            BookingDTO booking = adminService.createBooking(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Booking created successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/bookings/{id}")
    public ResponseEntity<ApiResponse<BookingDTO>> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookingRequest request) {
        try {
            BookingDTO booking = adminService.updateBooking(id, request);
            return ResponseEntity.ok(ApiResponse.success("Booking updated successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/bookings/{id}/status")
    public ResponseEntity<ApiResponse<BookingDTO>> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            BookingDTO booking = adminService.updateBookingStatus(id, status);
            return ResponseEntity.ok(ApiResponse.success("Booking status updated successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBooking(@PathVariable Long id) {
        try {
            adminService.deleteBooking(id);
            return ResponseEntity.ok(ApiResponse.success("Booking deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== Service Management Endpoints ====================
    
    @GetMapping("/services")
    public ResponseEntity<ApiResponse<Page<ServiceDTO>>> getAllServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<ServiceDTO> services = adminService.getAllServices(pageable);
            return ResponseEntity.ok(ApiResponse.success("Services retrieved successfully", services));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/services/{id}")
    public ResponseEntity<ApiResponse<ServiceDTO>> getServiceById(@PathVariable Long id) {
        try {
            ServiceDTO service = adminService.getServiceById(id);
            return ResponseEntity.ok(ApiResponse.success("Service retrieved successfully", service));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/services")
    public ResponseEntity<ApiResponse<ServiceDTO>> createService(@Valid @RequestBody CreateServiceRequest request) {
        try {
            ServiceDTO service = adminService.createService(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Service created successfully", service));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/services/{id}")
    public ResponseEntity<ApiResponse<ServiceDTO>> updateService(
            @PathVariable Long id,
            @Valid @RequestBody UpdateServiceRequest request) {
        try {
            ServiceDTO service = adminService.updateService(id, request);
            return ResponseEntity.ok(ApiResponse.success("Service updated successfully", service));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/services/{id}/toggle")
    public ResponseEntity<ApiResponse<ServiceDTO>> toggleServiceStatus(@PathVariable Long id) {
        try {
            ServiceDTO service = adminService.toggleServiceStatus(id);
            return ResponseEntity.ok(ApiResponse.success("Service status toggled successfully", service));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/services/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable Long id) {
        try {
            adminService.deleteService(id);
            return ResponseEntity.ok(ApiResponse.success("Service deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
