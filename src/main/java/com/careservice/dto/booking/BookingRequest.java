package com.careservice.dto.booking;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    
    @NotNull(message = "Service ID is required")
    private Long serviceId;
    
    private Long caregiverId; // Optional, can be assigned later
    
    @NotNull(message = "Scheduled start time is required")
    private LocalDateTime scheduledStartTime;
    
    @NotNull(message = "Scheduled end time is required")
    private LocalDateTime scheduledEndTime;
    
    private String customerNote;
    
    @NotNull(message = "Location is required")
    private String location;
}
