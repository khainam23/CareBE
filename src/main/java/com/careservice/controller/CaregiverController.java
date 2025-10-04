package com.careservice.controller;

import com.careservice.dto.ApiResponse;
import com.careservice.dto.booking.BookingDTO;
import com.careservice.dto.caregiver.CaregiverDTO;
import com.careservice.dto.caregiver.CaregiverProfileRequest;
import com.careservice.dto.review.ReviewDTO;
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
@RequestMapping("/api/caregiver")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CAREGIVER', 'ADMIN')")
public class CaregiverController {
    
    private final CaregiverService caregiverService;
    private final BookingService bookingService;
    private final ReviewService reviewService;
    private final SupportTicketService supportTicketService;
    
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<CaregiverDTO>> getMyProfile() {
        try {
            CaregiverDTO profile = caregiverService.getMyProfile();
            return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", profile));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<CaregiverDTO>> updateProfile(@Valid @RequestBody CaregiverProfileRequest request) {
        try {
            CaregiverDTO profile = caregiverService.updateProfile(request);
            return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", profile));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/availability")
    public ResponseEntity<ApiResponse<Void>> updateAvailability(@RequestBody Map<String, Boolean> request) {
        try {
            Boolean isAvailable = request.get("isAvailable");
            caregiverService.updateAvailability(isAvailable);
            return ResponseEntity.ok(ApiResponse.success("Availability updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<BookingDTO>>> getMyBookings() {
        try {
            List<BookingDTO> bookings = bookingService.getCaregiverBookings();
            return ResponseEntity.ok(ApiResponse.success("Bookings retrieved successfully", bookings));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/bookings/{id}/accept")
    public ResponseEntity<ApiResponse<BookingDTO>> acceptBooking(@PathVariable Long id) {
        try {
            BookingDTO booking = bookingService.acceptBooking(id);
            return ResponseEntity.ok(ApiResponse.success("Booking accepted successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/bookings/{id}/reject")
    public ResponseEntity<ApiResponse<BookingDTO>> rejectBooking(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String reason = request.get("reason");
            BookingDTO booking = bookingService.rejectBooking(id, reason);
            return ResponseEntity.ok(ApiResponse.success("Booking rejected", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/bookings/{id}/complete")
    public ResponseEntity<ApiResponse<BookingDTO>> completeBooking(@PathVariable Long id) {
        try {
            BookingDTO booking = bookingService.completeBooking(id);
            return ResponseEntity.ok(ApiResponse.success("Booking completed successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getMyReviews() {
        try {
            CaregiverDTO profile = caregiverService.getMyProfile();
            List<ReviewDTO> reviews = reviewService.getCaregiverReviews(profile.getId());
            return ResponseEntity.ok(ApiResponse.success("Reviews retrieved successfully", reviews));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/reviews/{id}/respond")
    public ResponseEntity<ApiResponse<ReviewDTO>> respondToReview(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String response = request.get("response");
            ReviewDTO review = reviewService.respondToReview(id, response);
            return ResponseEntity.ok(ApiResponse.success("Response submitted successfully", review));
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
}
