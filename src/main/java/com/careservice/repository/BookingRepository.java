package com.careservice.repository;

import com.careservice.entity.Booking;
import com.careservice.entity.Caregiver;
import com.careservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    Optional<Booking> findByBookingCode(String bookingCode);
    
    List<Booking> findByCustomer(Customer customer);
    
    List<Booking> findByCaregiver(Caregiver caregiver);
    
    List<Booking> findByStatus(Booking.BookingStatus status);
    
    List<Booking> findByCustomerAndStatus(Customer customer, Booking.BookingStatus status);
    
    List<Booking> findByCaregiverAndStatus(Caregiver caregiver, Booking.BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.caregiver IS NULL AND b.status = 'PENDING'")
    List<Booking> findUnassignedBookings();
    
    @Query("SELECT b FROM Booking b WHERE b.caregiver.id = :caregiverId " +
           "AND b.scheduledStartTime BETWEEN :startDate AND :endDate")
    List<Booking> findByCaregiverIdAndDateRange(@Param("caregiverId") Long caregiverId,
                                                  @Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = :status")
    Long countByStatus(Booking.BookingStatus status);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.customer.id = :customerId")
    Long countByCustomerId(Long customerId);
}
