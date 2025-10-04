package com.careservice.service;

import com.careservice.dto.admin.DashboardStatsDTO;
import com.careservice.dto.caregiver.CaregiverDTO;
import com.careservice.entity.Caregiver;
import com.careservice.entity.Role;
import com.careservice.entity.User;
import com.careservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    
    private final UserRepository userRepository;
    private final CaregiverRepository caregiverRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final SupportTicketRepository supportTicketRepository;
    private final NotificationService notificationService;
    
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        
        stats.setTotalUsers(userRepository.count());
        stats.setTotalCustomers(userRepository.countByRoleName("ROLE_CUSTOMER"));
        stats.setTotalCaregivers(userRepository.countByRoleName("ROLE_CAREGIVER"));
        stats.setPendingCaregivers(caregiverRepository.countByVerificationStatus(Caregiver.VerificationStatus.PENDING));
        stats.setApprovedCaregivers(caregiverRepository.countByVerificationStatus(Caregiver.VerificationStatus.APPROVED));
        
        stats.setTotalBookings(bookingRepository.count());
        stats.setPendingBookings(bookingRepository.countByStatus(com.careservice.entity.Booking.BookingStatus.PENDING));
        stats.setCompletedBookings(bookingRepository.countByStatus(com.careservice.entity.Booking.BookingStatus.COMPLETED));
        stats.setCancelledBookings(bookingRepository.countByStatus(com.careservice.entity.Booking.BookingStatus.CANCELLED));
        
        stats.setTotalPayments(paymentRepository.countCompletedPayments());
        Double totalRevenue = paymentRepository.getTotalRevenue();
        stats.setTotalRevenue(totalRevenue != null ? BigDecimal.valueOf(totalRevenue) : BigDecimal.ZERO);
        
        stats.setOpenTickets(supportTicketRepository.countByStatus(com.careservice.entity.SupportTicket.TicketStatus.OPEN));
        Long inProgress = supportTicketRepository.countByStatus(com.careservice.entity.SupportTicket.TicketStatus.IN_PROGRESS);
        Long escalated = supportTicketRepository.countByStatus(com.careservice.entity.SupportTicket.TicketStatus.ESCALATED);
        stats.setUnresolvedTickets((inProgress != null ? inProgress : 0L) + (escalated != null ? escalated : 0L));
        
        return stats;
    }
    
    @Transactional
    public CaregiverDTO approveCaregiver(Long caregiverId) {
        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new RuntimeException("Caregiver not found"));
        
        caregiver.setVerificationStatus(Caregiver.VerificationStatus.APPROVED);
        caregiver.setVerifiedAt(LocalDateTime.now());
        caregiver.setIsAvailable(true);
        
        User user = caregiver.getUser();
        user.setStatus(User.UserStatus.ACTIVE);
        userRepository.save(user);
        
        Caregiver savedCaregiver = caregiverRepository.save(caregiver);
        
        notificationService.createNotification(
                user,
                com.careservice.entity.Notification.NotificationType.ACCOUNT_VERIFIED,
                "Account Verified",
                "Your caregiver account has been approved. You can now start accepting bookings."
        );
        
        return convertToDTO(savedCaregiver);
    }
    
    @Transactional
    public CaregiverDTO rejectCaregiver(Long caregiverId, String reason) {
        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new RuntimeException("Caregiver not found"));
        
        caregiver.setVerificationStatus(Caregiver.VerificationStatus.REJECTED);
        caregiver.setRejectionReason(reason);
        
        User user = caregiver.getUser();
        user.setStatus(User.UserStatus.REJECTED);
        userRepository.save(user);
        
        Caregiver savedCaregiver = caregiverRepository.save(caregiver);
        
        notificationService.createNotification(
                user,
                com.careservice.entity.Notification.NotificationType.ACCOUNT_REJECTED,
                "Account Rejected",
                "Your caregiver application has been rejected. Reason: " + reason
        );
        
        return convertToDTO(savedCaregiver);
    }
    
    @Transactional
    public void suspendUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setStatus(User.UserStatus.SUSPENDED);
        user.setEnabled(false);
        userRepository.save(user);
    }
    
    @Transactional
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setStatus(User.UserStatus.ACTIVE);
        user.setEnabled(true);
        userRepository.save(user);
    }
    
    public List<CaregiverDTO> getPendingCaregivers() {
        return caregiverRepository.findByVerificationStatus(Caregiver.VerificationStatus.PENDING)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private CaregiverDTO convertToDTO(Caregiver caregiver) {
        CaregiverDTO dto = new CaregiverDTO();
        dto.setId(caregiver.getId());
        dto.setUserId(caregiver.getUser().getId());
        dto.setEmail(caregiver.getUser().getEmail());
        dto.setFullName(caregiver.getUser().getFullName());
        dto.setPhoneNumber(caregiver.getUser().getPhoneNumber());
        dto.setAddress(caregiver.getUser().getAddress());
        dto.setBio(caregiver.getBio());
        dto.setSkills(caregiver.getSkills());
        dto.setExperience(caregiver.getExperience());
        dto.setIdCardNumber(caregiver.getIdCardNumber());
        dto.setIdCardUrl(caregiver.getIdCardUrl());
        dto.setCertificateUrls(caregiver.getCertificateUrls());
        dto.setVerificationStatus(caregiver.getVerificationStatus().name());
        dto.setRejectionReason(caregiver.getRejectionReason());
        dto.setIsAvailable(caregiver.getIsAvailable());
        dto.setAvailableSchedule(caregiver.getAvailableSchedule());
        dto.setHourlyRate(caregiver.getHourlyRate());
        dto.setRating(caregiver.getRating());
        dto.setTotalReviews(caregiver.getTotalReviews());
        dto.setCompletedBookings(caregiver.getCompletedBookings());
        dto.setTotalEarnings(caregiver.getTotalEarnings());
        dto.setVerifiedAt(caregiver.getVerifiedAt());
        dto.setCreatedAt(caregiver.getCreatedAt());
        return dto;
    }
}
