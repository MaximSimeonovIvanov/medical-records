package com.medicalrecords.medical_records.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateSickLeaveRequest {

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @Min(value = 1, message = "Sick leave must be at least 1 day")
    private int numberOfDays;

    @NotNull(message = "Visit is required")
    private Long visitId;
    //болничният е винаги свързан към дадено посещение
}