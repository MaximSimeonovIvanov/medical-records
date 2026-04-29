package com.medicalrecords.medical_records.mapper;

import com.medicalrecords.medical_records.dto.response.*;
import com.medicalrecords.medical_records.entity.*;
import org.springframework.stereotype.Component;

@Component
//компонент казва на спринг да менажира класа така че да го инжектираме в сървисите
public class EntityMapper {

    public DoctorResponse toDoctorResponse(Doctor doctor) {
        DoctorResponse response = new DoctorResponse();
        response.setId(doctor.getId());
        response.setUin(doctor.getUin());
        response.setName(doctor.getName());
        response.setSpecialty(doctor.getSpecialty());
        response.setGp(doctor.isGp());
        return response;
    }

    public PatientResponse toPatientResponse(Patient patient) {
        PatientResponse response = new PatientResponse();
        response.setId(patient.getId());
        response.setName(patient.getName());
        response.setEgn(patient.getEgn());
        response.setHealthInsured(patient.isHealthInsured());
        if (patient.getGeneralPractitioner() != null) {
            response.setGeneralPractitionerId(
                    patient.getGeneralPractitioner().getId());
            response.setGeneralPractitionerName(
                    patient.getGeneralPractitioner().getName());
        }
        return response;
    }

    public DiagnosisResponse toDiagnosisResponse(Diagnosis diagnosis) {
        DiagnosisResponse response = new DiagnosisResponse();
        response.setId(diagnosis.getId());
        response.setName(diagnosis.getName());
        response.setDescription(diagnosis.getDescription());
        return response;
    }

    public VisitResponse toVisitResponse(Visit visit) {
        VisitResponse response = new VisitResponse();
        response.setId(visit.getId());
        response.setDate(visit.getDate());
        response.setDoctorName(visit.getDoctor().getName());
        response.setDoctorId(visit.getDoctor().getId());
        response.setPatientName(visit.getPatient().getName());
        response.setPatientId(visit.getPatient().getId());
        response.setTreatment(visit.getTreatment());
        response.setPrice(visit.getPrice());
        response.setPaidByNhif(visit.isPaidByNhif());
        if (visit.getDiagnosis() != null) {
            response.setDiagnosisName(visit.getDiagnosis().getName());
        }
        return response;
    }

    public SickLeaveResponse toSickLeaveResponse(SickLeave sickLeave) {
        SickLeaveResponse response = new SickLeaveResponse();
        response.setId(sickLeave.getId());
        response.setStartDate(sickLeave.getStartDate());
        response.setNumberOfDays(sickLeave.getNumberOfDays());
        response.setVisitId(sickLeave.getVisit().getId());
        response.setPatientName(sickLeave.getVisit().getPatient().getName());
        response.setDoctorName(sickLeave.getVisit().getDoctor().getName());
        return response;
    }
}