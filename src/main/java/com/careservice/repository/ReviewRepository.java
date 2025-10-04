package com.careservice.repository;

import com.careservice.entity.Caregiver;
import com.careservice.entity.Customer;
import com.careservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByCaregiver(Caregiver caregiver);
    
    List<Review> findByCustomer(Customer customer);
    
    @Query("SELECT r FROM Review r WHERE r.caregiver.id = :caregiverId ORDER BY r.createdAt DESC")
    List<Review> findByCaregiverIdOrderByCreatedAtDesc(Long caregiverId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.caregiver.id = :caregiverId")
    Double getAverageRatingByCaregiver(Long caregiverId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.caregiver.id = :caregiverId")
    Long countByCaregiver(Long caregiverId);
}
