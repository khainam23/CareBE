package com.careservice.repository;

import com.careservice.entity.SupportTicket;
import com.careservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    
    Optional<SupportTicket> findByTicketNumber(String ticketNumber);
    
    List<SupportTicket> findByUser(User user);
    
    List<SupportTicket> findByAssignedTo(User assignedTo);
    
    List<SupportTicket> findByStatus(SupportTicket.TicketStatus status);
    
    List<SupportTicket> findByCategory(SupportTicket.TicketCategory category);
    
    @Query("SELECT st FROM SupportTicket st WHERE st.assignedTo IS NULL AND st.status = 'OPEN'")
    List<SupportTicket> findUnassignedTickets();
    
    @Query("SELECT COUNT(st) FROM SupportTicket st WHERE st.status = :status")
    Long countByStatus(SupportTicket.TicketStatus status);
}
