package com.careservice.repository;

import com.careservice.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    
    Optional<ChatRoom> findByBookingId(Long bookingId);
    
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.customerId = :userId OR cr.caregiverId = :userId ORDER BY cr.lastMessageAt DESC NULLS LAST")
    List<ChatRoom> findAllByUserId(@Param("userId") Long userId);
    
    @Query("SELECT cr FROM ChatRoom cr WHERE (cr.customerId = :userId OR cr.caregiverId = :userId) AND cr.status = 'ACTIVE' ORDER BY cr.lastMessageAt DESC NULLS LAST")
    List<ChatRoom> findActiveByUserId(@Param("userId") Long userId);
    
    boolean existsByBookingId(Long bookingId);
    
    Optional<ChatRoom> findByCustomerIdAndCaregiverId(Long customerId, Long caregiverId);
}
