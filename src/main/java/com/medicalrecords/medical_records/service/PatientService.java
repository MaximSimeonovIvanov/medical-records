package com.medicalrecords.medical_records.service;

import com.medicalrecords.medical_records.dto.request.CreatePatientRequest;
import com.medicalrecords.medical_records.dto.response.PatientResponse;
import com.medicalrecords.medical_records.entity.Doctor;
import com.medicalrecords.medical_records.entity.Patient;
import com.medicalrecords.medical_records.exception.DuplicateResourceException;
import com.medicalrecords.medical_records.exception.ResourceNotFoundException;
import com.medicalrecords.medical_records.mapper.EntityMapper;
import com.medicalrecords.medical_records.repository.DoctorRepository;
import com.medicalrecords.medical_records.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final EntityMapper mapper;

    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(mapper::toPatientResponse)
                .toList();
    }

    public PatientResponse getPatientById(Long id) {
        return mapper.toPatientResponse(
                patientRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Patient with id " + id + " not found")));
    }

    public List<PatientResponse> getPatientsByGP(Long gpId) {
        return patientRepository.findByGeneralPractitionerId(gpId)
                .stream()
                .map(mapper::toPatientResponse)
                .toList();
    }

    @Transactional
    public PatientResponse createPatient(CreatePatientRequest request) {
        if (patientRepository.existsByEgn(request.getEgn())) {
            throw new DuplicateResourceException(
                    "Patient with EGN " + request.getEgn() + " already exists");
        }

        Doctor gp = doctorRepository.findById(request.getGeneralPractitionerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor with id " + request.getGeneralPractitionerId() + " not found"));

        if (!gp.isGp()) {
            throw new IllegalArgumentException(
                    "Doctor with id " + gp.getId() + " is not a general practitioner");
        }

        Patient patient = Patient.builder()
                .name(request.getName())
                .egn(request.getEgn())
                .healthInsured(request.isHealthInsured())
                .generalPractitioner(gp)
                .build();

        return mapper.toPatientResponse(patientRepository.save(patient));
    }

    @Transactional
    public PatientResponse updatePatient(Long id, CreatePatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient with id " + id + " not found"));

        Doctor gp = doctorRepository.findById(request.getGeneralPractitionerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor with id " + request.getGeneralPractitionerId() + " not found"));

        if (!gp.isGp()) {
            throw new IllegalArgumentException(
                    "Doctor with id " + gp.getId() + " is not a general practitioner");
        }

        patient.setName(request.getName());
        patient.setHealthInsured(request.isHealthInsured());
        patient.setGeneralPractitioner(gp);

        return mapper.toPatientResponse(patientRepository.save(patient));
    }

    @Transactional
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Patient with id " + id + " not found");
        }
        patientRepository.deleteById(id);
    }
}