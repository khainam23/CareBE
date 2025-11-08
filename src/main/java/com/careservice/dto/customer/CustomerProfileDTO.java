package com.careservice.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private String gender;
    private String notes;
    private String emergencyContact;
    private String emergencyPhone;
}
