package com.careservice.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    
    private Long id;
    private String bookingCode;
    private Long customerId;
    private String customerName;
    private Long caregiverId;
    private String caregiverName;
    private Long serviceId;
    private String serviceName;
    private LocalDateTime scheduledStartTime;
    private LocalDateTime scheduledEndTime;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    private BigDecimal totalPrice;
    private String status;
    private String customerNote;
    private String caregiverNote;
    private String location;
    private String cancellationReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
