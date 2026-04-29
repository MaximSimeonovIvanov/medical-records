package com.medicalrecords.medical_records.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SickLeaveResponse {
    private Long id;
    private LocalDate startDate;
    private int numberOfDays;
    private Long visitId;
    private String patientName;
    private String doctorName;
}