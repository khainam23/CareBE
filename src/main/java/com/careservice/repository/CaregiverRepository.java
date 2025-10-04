package com.careservice.repository;

import com.careservice.entity.Caregiver;
import com.careservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {
    
    Optional<Caregiver> findByUser(User user);
    
    Optional<Caregiver> findByUserId(Long userId);
    
    List<Caregiver> findByVerificationStatus(Caregiver.VerificationStatus status);
    
    List<Caregiver> findByIsAvailableTrue();
    
    @Query("SELECT c FROM Caregiver c WHERE c.verificationStatus = 'APPROVED' AND c.isAvailable = true")
    List<Caregiver> findAvailableApprovedCaregivers();
    
    @Query("SELECT COUNT(c) FROM Caregiver c WHERE c.verificationStatus = :status")
    Long countByVerificationStatus(Caregiver.VerificationStatus status);
}
