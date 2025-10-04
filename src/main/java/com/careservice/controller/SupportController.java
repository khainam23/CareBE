package com.careservice.controller;

import com.careservice.dto.ApiResponse;
import com.careservice.dto.support.SupportTicketDTO;
import com.careservice.dto.support.SupportTicketRequest;
import com.careservice.service.SupportTicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/support")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPPORT', 'ADMIN')")
public class SupportController {
    
    private final SupportTicketService supportTicketService;
    
    @GetMapping("/tickets")
    public ResponseEntity<ApiResponse<List<SupportTicketDTO>>> getAllTickets() {
        try {
            List<SupportTicketDTO> tickets = supportTicketService.getAllTickets();
            return ResponseEntity.ok(ApiResponse.success("All tickets retrieved successfully", tickets));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/tickets/unassigned")
    public ResponseEntity<ApiResponse<List<SupportTicketDTO>>> getUnassignedTickets() {
        try {
            List<SupportTicketDTO> tickets = supportTicketService.getUnassignedTickets();
            return ResponseEntity.ok(ApiResponse.success("Unassigned tickets retrieved successfully", tickets));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/tickets/assigned")
    public ResponseEntity<ApiResponse<List<SupportTicketDTO>>> getAssignedTickets() {
        try {
            List<SupportTicketDTO> tickets = supportTicketService.getAssignedTickets();
            return ResponseEntity.ok(ApiResponse.success("Assigned tickets retrieved successfully", tickets));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/tickets/{id}/assign")
    public ResponseEntity<ApiResponse<SupportTicketDTO>> assignTicket(
            @PathVariable Long id,
            @RequestBody Map<String, Long> request) {
        try {
            Long supportUserId = request.get("supportUserId");
            SupportTicketDTO ticket = supportTicketService.assignTicket(id, supportUserId);
            return ResponseEntity.ok(ApiResponse.success("Ticket assigned successfully", ticket));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/tickets/{id}/status")
    public ResponseEntity<ApiResponse<SupportTicketDTO>> updateTicketStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            SupportTicketDTO ticket = supportTicketService.updateTicketStatus(id, status);
            return ResponseEntity.ok(ApiResponse.success("Ticket status updated successfully", ticket));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/tickets/{id}/resolve")
    public ResponseEntity<ApiResponse<SupportTicketDTO>> resolveTicket(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String resolution = request.get("resolution");
            SupportTicketDTO ticket = supportTicketService.resolveTicket(id, resolution);
            return ResponseEntity.ok(ApiResponse.success("Ticket resolved successfully", ticket));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/tickets/{id}/escalate")
    public ResponseEntity<ApiResponse<SupportTicketDTO>> escalateTicket(@PathVariable Long id) {
        try {
            SupportTicketDTO ticket = supportTicketService.escalateTicket(id);
            return ResponseEntity.ok(ApiResponse.success("Ticket escalated successfully", ticket));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
