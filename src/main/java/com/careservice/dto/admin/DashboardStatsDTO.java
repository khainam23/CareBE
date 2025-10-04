package com.careservice.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    
    private Long totalUsers;
    private Long totalCustomers;
    private Long totalCaregivers;
    private Long pendingCaregivers;
    private Long approvedCaregivers;
    private Long totalBookings;
    private Long pendingBookings;
    private Long completedBookings;
    private Long cancelledBookings;
    private Long totalPayments;
    private BigDecimal totalRevenue;
    private Long openTickets;
    private Long unresolvedTickets;
}
