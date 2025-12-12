package com.careservice.service;

import com.careservice.dto.caregiver.CaregiverDTO;
import com.careservice.dto.caregiver.CaregiverProfileRequest;
import com.careservice.entity.Caregiver;
import com.careservice.entity.User;
import com.careservice.repository.CaregiverRepository;
import com.careservice.repository.UserRepository;
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
public class CaregiverService {
    
    private final CaregiverRepository caregiverRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public CaregiverDTO updateProfile(CaregiverProfileRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Caregiver caregiver = caregiverRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Caregiver profile not found"));
        
        caregiver.setBio(request.getBio());
        caregiver.setSkills(request.getSkills());
        caregiver.setExperience(request.getExperience());
        caregiver.setIdCardNumber(request.getIdCardNumber());
        caregiver.setIdCardUrl(request.getIdCardUrl());
        caregiver.setCertificateUrls(request.getCertificateUrls());
        caregiver.setAvailableSchedule(request.getAvailableSchedule());
        caregiver.setHourlyRate(request.getHourlyRate());
        
        Caregiver savedCaregiver = caregiverRepository.save(caregiver);
        
        return convertToDTO(savedCaregiver);
    }
    
    public CaregiverDTO getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Caregiver caregiver = caregiverRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Caregiver profile not found"));
        
        return convertToDTO(caregiver);
    }
    
    public CaregiverDTO getCaregiverById(Long id) {
        Caregiver caregiver = caregiverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Caregiver not found"));
        
        return convertToDTO(caregiver);
    }
    
    public List<CaregiverDTO> getAvailableCaregivers() {
        return caregiverRepository.findAvailableApprovedCaregivers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void updateAvailability(Boolean isAvailable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Caregiver caregiver = caregiverRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Caregiver profile not found"));
        
        caregiver.setIsAvailable(isAvailable);
        caregiverRepository.save(caregiver);
    }
    
    public java.util.Map<String, Object> getDashboardStats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Caregiver caregiver = caregiverRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Caregiver profile not found"));
        
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("monthlyEarnings", caregiver.getTotalEarnings() != null ? caregiver.getTotalEarnings() : 0);
        stats.put("earningsGrowth", 0.0);
        stats.put("weeklyHours", 0);
        stats.put("todayBookings", 0);
        stats.put("upcomingAppointments", new java.util.ArrayList<>());
        stats.put("recentPatients", new java.util.ArrayList<>());
        
        return stats;
    }
    
    private CaregiverDTO convertToDTO(Caregiver caregiver) {
        CaregiverDTO dto = new CaregiverDTO();
        dto.setId(caregiver.getId());
        dto.setUserId(caregiver.getUser().getId());
        dto.setEmail(caregiver.getUser().getEmail());
        dto.setFullName(caregiver.getUser().getFullName());
        dto.setPhoneNumber(caregiver.getUser().getPhoneNumber());
        dto.setAddress(caregiver.getUser().getAddress());
        dto.setAvatarUrl(caregiver.getUser().getAvatarUrl());
        dto.setBio(caregiver.getBio());
        dto.setSkills(caregiver.getSkills());
        dto.setExperience(caregiver.getExperience());
        dto.setExperienceYears(caregiver.getExperienceYears());
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
