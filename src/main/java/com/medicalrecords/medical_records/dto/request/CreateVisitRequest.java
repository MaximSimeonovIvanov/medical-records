package com.medicalrecords.medical_records.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateVisitRequest {

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Patient is required")
    private Long patientId;

    @NotNull(message = "Diagnosis is required")
    private Long diagnosisId;

    private String treatment;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    //bez doctorId zashtoto tuk se opredelq ot JWT tokena na lognatiq user. Lekarqt moje da sazdava viziti samo za sebe si
}