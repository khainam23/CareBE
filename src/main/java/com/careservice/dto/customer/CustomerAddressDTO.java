package com.careservice.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddressDTO {
    private Long id;
    private String address;
    private String label;
    private Boolean isDefault;
    private LocalDateTime createdAt;
}
