package com.medicalrecords.medical_records.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponse {
    private Long id;
    private String name;
    private String egn;
    private boolean healthInsured;
    private Long generalPractitionerId;
    private String generalPractitionerName; //за да не прави пациента клиента втори request за името на лекаря
}