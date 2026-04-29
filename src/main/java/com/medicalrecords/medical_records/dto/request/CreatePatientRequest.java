package com.medicalrecords.medical_records.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreatePatientRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "EGN is required")
    @Pattern(regexp = "\\d{10}", message = "EGN must be exactly 10 digits")
    private String egn;

    private boolean healthInsured;

    @NotNull(message = "General practitioner is required")
    private Long generalPractitionerId;
}