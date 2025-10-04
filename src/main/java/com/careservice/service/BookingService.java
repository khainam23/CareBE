package com.careservice.service;

import com.careservice.dto.booking.BookingDTO;
import com.careservice.dto.booking.BookingRequest;
import com.careservice.entity.*;
import com.careservice.repository.*;
import com.careservice.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final CaregiverRepository caregiverRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    
    @Transactional
    public BookingDTO createBooking(BookingRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Customer profile not found"));
        
        com.careservice.entity.Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));
        
        Booking booking = new Booking();
        booking.setBookingCode(generateBookingCode());
        booking.setCustomer(customer);
        booking.setService(service);
        booking.setScheduledStartTime(request.getScheduledStartTime());
        booking.setScheduledEndTime(request.getScheduledEndTime());
        booking.setCustomerNote(request.getCustomerNote());
        booking.setLocation(request.getLocation());
        booking.setStatus(Booking.BookingStatus.PENDING);
        
        // Calculate total price based on duration and service base price
        Duration duration = Duration.between(request.getScheduledStartTime(), request.getScheduledEndTime());
        long hours = duration.toHours();
        if (hours < 1) hours = 1;
        booking.setTotalPrice(service.getBasePrice().multiply(BigDecimal.valueOf(hours)));
        
        // Assign caregiver if specified
        if (request.getCaregiverId() != null) {
            Caregiver caregiver = caregiverRepository.findById(request.getCaregiverId())
                    .orElseThrow(() -> new RuntimeException("Caregiver not found"));
            
            if (!caregiver.getVerificationStatus().equals(Caregiver.VerificationStatus.APPROVED)) {
                throw new RuntimeException("Caregiver is not verified");
            }
            
            booking.setCaregiver(caregiver);
            booking.setStatus(Booking.BookingStatus.ASSIGNED);
            
            notificationService.createNotification(
                    caregiver.getUser(),
                    Notification.NotificationType.NEW_BOOKING_REQUEST,
                    "New Booking Request",
                    "You have received a new booking request for " + service.getName()
            );
        }
        
        Booking savedBooking = bookingRepository.save(booking);
        
        notificationService.createNotification(
                user,
                Notification.NotificationType.BOOKING_CONFIRMATION,
                "Booking Created",
                "Your booking has been created successfully. Booking code: " + booking.getBookingCode()
        );
        
        return convertToDTO(savedBooking);
    }
    
    public List<BookingDTO> getCustomerBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Customer profile not found"));
        
        return bookingRepository.findByCustomer(customer).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<BookingDTO> getCaregiverBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Caregiver caregiver = caregiverRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Caregiver profile not found"));
        
        return bookingRepository.findByCaregiver(caregiver).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public BookingDTO acceptBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        Booking savedBooking = bookingRepository.save(booking);
        
        notificationService.createNotification(
                booking.getCustomer().getUser(),
                Notification.NotificationType.CAREGIVER_ASSIGNED,
                "Booking Confirmed",
                "Your booking has been confirmed by the caregiver"
        );
        
        return convertToDTO(savedBooking);
    }
    
    @Transactional
    public BookingDTO rejectBooking(Long bookingId, String reason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setStatus(Booking.BookingStatus.REJECTED);
        booking.setCancellationReason(reason);
        booking.setCancelledAt(LocalDateTime.now());
        Booking savedBooking = bookingRepository.save(booking);
        
        notificationService.createNotification(
                booking.getCustomer().getUser(),
                Notification.NotificationType.BOOKING_CANCELLED,
                "Booking Rejected",
                "Your booking has been rejected. Reason: " + reason
        );
        
        return convertToDTO(savedBooking);
    }
    
    @Transactional
    public BookingDTO cancelBooking(Long bookingId, String reason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.setCancellationReason(reason);
        booking.setCancelledAt(LocalDateTime.now());
        Booking savedBooking = bookingRepository.save(booking);
        
        if (booking.getCaregiver() != null) {
            notificationService.createNotification(
                    booking.getCaregiver().getUser(),
                    Notification.NotificationType.BOOKING_CANCELLED,
                    "Booking Cancelled",
                    "A booking has been cancelled by the customer"
            );
        }
        
        return convertToDTO(savedBooking);
    }
    
    @Transactional
    public BookingDTO completeBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setStatus(Booking.BookingStatus.COMPLETED);
        booking.setActualEndTime(LocalDateTime.now());
        Booking savedBooking = bookingRepository.save(booking);
        
        // Update caregiver statistics
        if (booking.getCaregiver() != null) {
            Caregiver caregiver = booking.getCaregiver();
            caregiver.setCompletedBookings(caregiver.getCompletedBookings() + 1);
            caregiver.setTotalEarnings(caregiver.getTotalEarnings().add(booking.getTotalPrice()));
            caregiverRepository.save(caregiver);
        }
        
        // Update customer statistics
        Customer customer = booking.getCustomer();
        customer.setBookingCount(customer.getBookingCount() + 1);
        customer.setTotalSpent(customer.getTotalSpent().add(booking.getTotalPrice()));
        customerRepository.save(customer);
        
        notificationService.createNotification(
                booking.getCustomer().getUser(),
                Notification.NotificationType.BOOKING_COMPLETED,
                "Booking Completed",
                "Your booking has been completed. Please leave a review."
        );
        
        return convertToDTO(savedBooking);
    }
    
    private String generateBookingCode() {
        return "BK" + System.currentTimeMillis() + new Random().nextInt(1000);
    }
    
    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setBookingCode(booking.getBookingCode());
        dto.setCustomerId(booking.getCustomer().getId());
        dto.setCustomerName(booking.getCustomer().getUser().getFullName());
        
        if (booking.getCaregiver() != null) {
            dto.setCaregiverId(booking.getCaregiver().getId());
            dto.setCaregiverName(booking.getCaregiver().getUser().getFullName());
        }
        
        dto.setServiceId(booking.getService().getId());
        dto.setServiceName(booking.getService().getName());
        dto.setScheduledStartTime(booking.getScheduledStartTime());
        dto.setScheduledEndTime(booking.getScheduledEndTime());
        dto.setActualStartTime(booking.getActualStartTime());
        dto.setActualEndTime(booking.getActualEndTime());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setStatus(booking.getStatus().name());
        dto.setCustomerNote(booking.getCustomerNote());
        dto.setCaregiverNote(booking.getCaregiverNote());
        dto.setLocation(booking.getLocation());
        dto.setCancellationReason(booking.getCancellationReason());
        dto.setCreatedAt(booking.getCreatedAt());
        dto.setUpdatedAt(booking.getUpdatedAt());
        
        return dto;
    }
}
