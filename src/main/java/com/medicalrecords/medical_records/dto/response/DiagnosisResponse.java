package com.medicalrecords.medical_records.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosisResponse {
    private Long id;
    private String name;
    private String description;
}