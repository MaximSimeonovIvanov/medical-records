package com.medicalrecords.medical_records.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitResponse {
    private Long id;
    private LocalDate date;
    private String doctorName;
    private Long doctorId;
    private String patientName;
    private Long patientId;
    private String diagnosisName;
    private String treatment;
    private BigDecimal price;
    private boolean paidByNhif;
    //imena vmesto nested objects
    // lesno se polzwa ot klienta
}