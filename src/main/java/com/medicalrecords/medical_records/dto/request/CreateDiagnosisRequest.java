package com.medicalrecords.medical_records.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateDiagnosisRequest {

    @NotBlank(message = "Diagnosis name is required")
    private String name;

    private String description;
}