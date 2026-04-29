package com.medicalrecords.medical_records.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse {
    private Long id;
    private String uin;
    private String name;
    private String specialty;
    private boolean gp;
}