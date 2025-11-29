package com.careservice.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VNPayResponse {
    
    private String paymentUrl;
    private String transactionId;
    private Long bookingId;
    private Long amount;
    private String orderInfo;
    private String createTime;
}
