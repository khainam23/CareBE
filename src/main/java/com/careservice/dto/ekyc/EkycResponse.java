package com.careservice.dto.ekyc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EkycResponse {
    private String idNumber;
    private String name;
    private String dob;
    private String sex;
    private String nationality;
    private String homeAddress;
    private String residenceAddress;
    private String expiryDate;
}
