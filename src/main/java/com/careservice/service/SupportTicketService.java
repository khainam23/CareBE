package com.careservice.service;

import com.careservice.dto.support.SupportTicketDTO;
import com.careservice.dto.support.SupportTicketRequest;
import com.careservice.entity.SupportTicket;
import com.careservice.entity.User;
import com.careservice.repository.SupportTicketRepository;
import com.careservice.repository.UserRepository;
import com.careservice.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportTicketService {
    
    private final SupportTicketRepository supportTicketRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    
    @Transactional
    public SupportTicketDTO createTicket(SupportTicketRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        SupportTicket ticket = new SupportTicket();
        ticket.setTicketNumber(generateTicketNumber());
        ticket.setUser(user);
        ticket.setCategory(SupportTicket.TicketCategory.valueOf(request.getCategory()));
        ticket.setPriority(request.getPriority() != null ? 
                SupportTicket.Priority.valueOf(request.getPriority()) : 
                SupportTicket.Priority.MEDIUM);
        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setStatus(SupportTicket.TicketStatus.OPEN);
        
        SupportTicket savedTicket = supportTicketRepository.save(ticket);
        
        return convertToDTO(savedTicket);
    }
    
    public List<SupportTicketDTO> getUserTickets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return supportTicketRepository.findByUser(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<SupportTicketDTO> getAllTickets() {
        return supportTicketRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<SupportTicketDTO> getUnassignedTickets() {
        return supportTicketRepository.findUnassignedTickets().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<SupportTicketDTO> getAssignedTickets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return supportTicketRepository.findByAssignedTo(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public SupportTicketDTO assignTicket(Long ticketId, Long supportUserId) {
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        User supportUser = userRepository.findById(supportUserId)
                .orElseThrow(() -> new RuntimeException("Support user not found"));
        
        ticket.setAssignedTo(supportUser);
        ticket.setStatus(SupportTicket.TicketStatus.IN_PROGRESS);
        
        SupportTicket savedTicket = supportTicketRepository.save(ticket);
        
        notificationService.createNotification(
                ticket.getUser(),
                com.careservice.entity.Notification.NotificationType.SUPPORT_TICKET_UPDATE,
                "Ticket Assigned",
                "Your support ticket has been assigned to a support agent"
        );
        
        return convertToDTO(savedTicket);
    }
    
    @Transactional
    public SupportTicketDTO updateTicketStatus(Long ticketId, String status) {
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        ticket.setStatus(SupportTicket.TicketStatus.valueOf(status));
        
        if (status.equals("RESOLVED")) {
            ticket.setResolvedAt(LocalDateTime.now());
        } else if (status.equals("CLOSED")) {
            ticket.setClosedAt(LocalDateTime.now());
        }
        
        SupportTicket savedTicket = supportTicketRepository.save(ticket);
        
        notificationService.createNotification(
                ticket.getUser(),
                com.careservice.entity.Notification.NotificationType.SUPPORT_TICKET_UPDATE,
                "Ticket Status Updated",
                "Your support ticket status has been updated to " + status
        );
        
        return convertToDTO(savedTicket);
    }
    
    @Transactional
    public SupportTicketDTO resolveTicket(Long ticketId, String resolution) {
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        ticket.setResolution(resolution);
        ticket.setStatus(SupportTicket.TicketStatus.RESOLVED);
        ticket.setResolvedAt(LocalDateTime.now());
        
        SupportTicket savedTicket = supportTicketRepository.save(ticket);
        
        notificationService.createNotification(
                ticket.getUser(),
                com.careservice.entity.Notification.NotificationType.SUPPORT_TICKET_UPDATE,
                "Ticket Resolved",
                "Your support ticket has been resolved: " + resolution
        );
        
        return convertToDTO(savedTicket);
    }
    
    @Transactional
    public SupportTicketDTO escalateTicket(Long ticketId) {
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        ticket.setStatus(SupportTicket.TicketStatus.ESCALATED);
        ticket.setPriority(SupportTicket.Priority.HIGH);
        
        SupportTicket savedTicket = supportTicketRepository.save(ticket);
        
        return convertToDTO(savedTicket);
    }
    
    private String generateTicketNumber() {
        return "TKT" + System.currentTimeMillis() + new Random().nextInt(1000);
    }
    
    private SupportTicketDTO convertToDTO(SupportTicket ticket) {
        SupportTicketDTO dto = new SupportTicketDTO();
        dto.setId(ticket.getId());
        dto.setTicketNumber(ticket.getTicketNumber());
        dto.setUserId(ticket.getUser().getId());
        dto.setUserName(ticket.getUser().getFullName());
        
        if (ticket.getAssignedTo() != null) {
            dto.setAssignedToId(ticket.getAssignedTo().getId());
            dto.setAssignedToName(ticket.getAssignedTo().getFullName());
        }
        
        dto.setCategory(ticket.getCategory().name());
        dto.setPriority(ticket.getPriority().name());
        dto.setSubject(ticket.getSubject());
        dto.setDescription(ticket.getDescription());
        dto.setStatus(ticket.getStatus().name());
        dto.setResolution(ticket.getResolution());
        dto.setResolvedAt(ticket.getResolvedAt());
        dto.setClosedAt(ticket.getClosedAt());
        dto.setCreatedAt(ticket.getCreatedAt());
        dto.setUpdatedAt(ticket.getUpdatedAt());
        
        return dto;
    }
}
