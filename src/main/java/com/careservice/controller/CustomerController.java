package com.careservice.controller;

import com.careservice.dto.ApiResponse;
import com.careservice.dto.booking.BookingDTO;
import com.careservice.dto.booking.BookingRequest;
import com.careservice.dto.payment.PaymentDTO;
import com.careservice.dto.payment.PaymentRequest;
import com.careservice.dto.review.ReviewDTO;
import com.careservice.dto.review.ReviewRequest;
import com.careservice.dto.support.SupportTicketDTO;
import com.careservice.dto.support.SupportTicketRequest;
import com.careservice.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
public class CustomerController {
    
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final ReviewService reviewService;
    private final SupportTicketService supportTicketService;
    private final CaregiverService caregiverService;
    private final AdminService adminService;
    
    @PostMapping("/bookings")
    public ResponseEntity<ApiResponse<BookingDTO>> createBooking(@Valid @RequestBody BookingRequest request) {
        try {
            BookingDTO booking = bookingService.createBooking(request);
            return ResponseEntity.ok(ApiResponse.success("Booking created successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<BookingDTO>>> getMyBookings() {
        try {
            List<BookingDTO> bookings = bookingService.getCustomerBookings();
            return ResponseEntity.ok(ApiResponse.success("Bookings retrieved successfully", bookings));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/bookings/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingDTO>> cancelBooking(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String reason = request.get("reason");
            BookingDTO booking = bookingService.cancelBooking(id, reason);
            return ResponseEntity.ok(ApiResponse.success("Booking cancelled successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/payments")
    public ResponseEntity<ApiResponse<PaymentDTO>> createPayment(@Valid @RequestBody PaymentRequest request) {
        try {
            PaymentDTO payment = paymentService.createPayment(request);
            return ResponseEntity.ok(ApiResponse.success("Payment processed successfully", payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/payments/booking/{bookingId}")
    public ResponseEntity<ApiResponse<PaymentDTO>> getPaymentByBooking(@PathVariable Long bookingId) {
        try {
            PaymentDTO payment = paymentService.getPaymentByBookingId(bookingId);
            return ResponseEntity.ok(ApiResponse.success("Payment retrieved successfully", payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/reviews")
    public ResponseEntity<ApiResponse<ReviewDTO>> createReview(@Valid @RequestBody ReviewRequest request) {
        try {
            ReviewDTO review = reviewService.createReview(request);
            return ResponseEntity.ok(ApiResponse.success("Review submitted successfully", review));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/caregivers")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<List<com.careservice.dto.caregiver.CaregiverDTO>>> getAvailableCaregivers() {
        try {
            List<com.careservice.dto.caregiver.CaregiverDTO> caregivers = caregiverService.getAvailableCaregivers();
            return ResponseEntity.ok(ApiResponse.success("Available caregivers retrieved successfully", caregivers));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/caregivers/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<com.careservice.dto.caregiver.CaregiverDTO>> getCaregiverById(@PathVariable Long id) {
        try {
            com.careservice.dto.caregiver.CaregiverDTO caregiver = caregiverService.getCaregiverById(id);
            return ResponseEntity.ok(ApiResponse.success("Caregiver retrieved successfully", caregiver));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/caregivers/{id}/reviews")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getCaregiverReviews(@PathVariable Long id) {
        try {
            List<ReviewDTO> reviews = reviewService.getCaregiverReviews(id);
            return ResponseEntity.ok(ApiResponse.success("Reviews retrieved successfully", reviews));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/support/tickets")
    public ResponseEntity<ApiResponse<SupportTicketDTO>> createSupportTicket(@Valid @RequestBody SupportTicketRequest request) {
        try {
            SupportTicketDTO ticket = supportTicketService.createTicket(request);
            return ResponseEntity.ok(ApiResponse.success("Support ticket created successfully", ticket));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/support/tickets")
    public ResponseEntity<ApiResponse<List<SupportTicketDTO>>> getMySupportTickets() {
        try {
            List<SupportTicketDTO> tickets = supportTicketService.getUserTickets();
            return ResponseEntity.ok(ApiResponse.success("Support tickets retrieved successfully", tickets));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/services")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<List<com.careservice.dto.admin.ServiceDTO>>> getActiveServices() {
        try {
            List<com.careservice.dto.admin.ServiceDTO> services = adminService.getActiveServices();
            return ResponseEntity.ok(ApiResponse.success("Services retrieved successfully", services));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
