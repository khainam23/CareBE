package com.careservice.service;

import com.careservice.dto.review.ReviewDTO;
import com.careservice.dto.review.ReviewRequest;
import com.careservice.entity.*;
import com.careservice.repository.*;
import com.careservice.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final CaregiverRepository caregiverRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    
    @Transactional
    public ReviewDTO createReview(ReviewRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Customer profile not found"));
        
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        if (!booking.getStatus().equals(Booking.BookingStatus.COMPLETED)) {
            throw new RuntimeException("Can only review completed bookings");
        }
        
        if (!booking.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("You can only review your own bookings");
        }
        
        if (booking.getCaregiver() == null) {
            throw new RuntimeException("No caregiver assigned to this booking");
        }
        
        Review review = new Review();
        review.setBooking(booking);
        review.setCustomer(customer);
        review.setCaregiver(booking.getCaregiver());
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        
        Review savedReview = reviewRepository.save(review);
        
        // Update caregiver rating
        updateCaregiverRating(booking.getCaregiver());
        
        notificationService.createNotification(
                booking.getCaregiver().getUser(),
                Notification.NotificationType.REVIEW_RECEIVED,
                "New Review",
                "You have received a new review with " + request.getRating() + " stars"
        );
        
        return convertToDTO(savedReview);
    }
    
    @Transactional
    public ReviewDTO respondToReview(Long reviewId, String response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Caregiver caregiver = caregiverRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Caregiver profile not found"));
        
        if (!review.getCaregiver().getId().equals(caregiver.getId())) {
            throw new RuntimeException("You can only respond to your own reviews");
        }
        
        review.setCaregiverResponse(response);
        review.setRespondedAt(java.time.LocalDateTime.now());
        
        Review savedReview = reviewRepository.save(review);
        
        return convertToDTO(savedReview);
    }
    
    public List<ReviewDTO> getCaregiverReviews(Long caregiverId) {
        return reviewRepository.findByCaregiverIdOrderByCreatedAtDesc(caregiverId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    private void updateCaregiverRating(Caregiver caregiver) {
        Double averageRating = reviewRepository.getAverageRatingByCaregiver(caregiver.getId());
        Long totalReviews = reviewRepository.countByCaregiver(caregiver.getId());
        
        caregiver.setRating(averageRating != null ? averageRating : 0.0);
        caregiver.setTotalReviews(totalReviews != null ? totalReviews.intValue() : 0);
        
        caregiverRepository.save(caregiver);
    }
    
    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setBookingId(review.getBooking().getId());
        dto.setBookingCode(review.getBooking().getBookingCode());
        dto.setCustomerId(review.getCustomer().getId());
        dto.setCustomerName(review.getCustomer().getUser().getFullName());
        dto.setCaregiverId(review.getCaregiver().getId());
        dto.setCaregiverName(review.getCaregiver().getUser().getFullName());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCaregiverResponse(review.getCaregiverResponse());
        dto.setRespondedAt(review.getRespondedAt());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }
}
