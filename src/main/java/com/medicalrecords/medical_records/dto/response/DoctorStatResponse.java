package com.medicalrecords.medical_records.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DoctorStatResponse {
    private String doctorName;
    private Long count;
    private BigDecimal totalAmount;
    //count = бр. посещ. или болнични
    //totalamount = пари (null when not applicable)
}