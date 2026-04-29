package com.medicalrecords.medical_records.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateDoctorRequest {

    @NotBlank(message = "UIN is required")
    @Size(min = 10, max = 10, message = "UIN must be exactly 10 characters")
    private String uin;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Specialty is required")
    private String specialty;

    private boolean gp;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}