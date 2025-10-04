package com.careservice.dto.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupportTicketDTO {
    
    private Long id;
    private String ticketNumber;
    private Long userId;
    private String userName;
    private Long assignedToId;
    private String assignedToName;
    private String category;
    private String priority;
    private String subject;
    private String description;
    private String status;
    private String resolution;
    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
