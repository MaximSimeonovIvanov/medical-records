package com.medicalrecords.medical_records.service;

import com.medicalrecords.medical_records.dto.request.CreateVisitRequest;
import com.medicalrecords.medical_records.dto.response.VisitResponse;
import com.medicalrecords.medical_records.entity.Diagnosis;
import com.medicalrecords.medical_records.entity.Doctor;
import com.medicalrecords.medical_records.entity.Patient;
import com.medicalrecords.medical_records.entity.Visit;
import com.medicalrecords.medical_records.exception.ResourceNotFoundException;
import com.medicalrecords.medical_records.mapper.EntityMapper;
import com.medicalrecords.medical_records.repository.DiagnosisRepository;
import com.medicalrecords.medical_records.repository.DoctorRepository;
import com.medicalrecords.medical_records.repository.PatientRepository;
import com.medicalrecords.medical_records.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final EntityMapper mapper;

    public List<VisitResponse> getAllVisits() {
        return visitRepository.findAll()
                .stream()
                .map(mapper::toVisitResponse)
                .toList();
    }

    public VisitResponse getVisitById(Long id) {
        return mapper.toVisitResponse(
                visitRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Visit with id " + id + " not found")));
    }

    public List<VisitResponse> getVisitsByPatient(Long patientId) {
        return visitRepository.findByPatientId(patientId)
                .stream()
                .map(mapper::toVisitResponse)
                .toList();
    }

    public List<VisitResponse> getVisitsByDoctor(Long doctorId) {
        return visitRepository.findByDoctorId(doctorId)
                .stream()
                .map(mapper::toVisitResponse)
                .toList();
    }

    public List<VisitResponse> getVisitsByDoctorAndPeriod(
            Long doctorId, LocalDate from, LocalDate to) {
        return visitRepository.findByDoctorIdAndDateBetween(doctorId, from, to)
                .stream()
                .map(mapper::toVisitResponse)
                .toList();
    }

    @Transactional
    public VisitResponse createVisit(CreateVisitRequest request, String username) {
        // намира доктора автоматично по JWT token
        Doctor doctor = doctorRepository.findAll()
                .stream()
                .filter(d -> d.getVisits() != null)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        // сега намира лекар по връзката с пациент. todo: да измисля нещо по-добро
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient with id " + request.getPatientId() + " not found"));

        Diagnosis diagnosis = diagnosisRepository.findById(request.getDiagnosisId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Diagnosis with id " + request.getDiagnosisId() + " not found"));

        //ако пациента е застрахован, плаща НЗОК
        boolean paidByNhif = patient.isHealthInsured();

        Visit visit = Visit.builder()
                .date(request.getDate())
                .doctor(doctor)
                .patient(patient)
                .diagnosis(diagnosis)
                .treatment(request.getTreatment())
                .price(request.getPrice())
                .paidByNhif(paidByNhif)
                .build();

        return mapper.toVisitResponse(visitRepository.save(visit));
    }

    @Transactional
    public VisitResponse updateVisit(Long id, CreateVisitRequest request, String username) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Visit with id " + id + " not found"));

        // лекарите трябва да редактират само свои посещения
        Diagnosis diagnosis = diagnosisRepository.findById(request.getDiagnosisId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Diagnosis with id " + request.getDiagnosisId() + " not found"));

        visit.setDate(request.getDate());
        visit.setDiagnosis(diagnosis);
        visit.setTreatment(request.getTreatment());
        visit.setPrice(request.getPrice());

        return mapper.toVisitResponse(visitRepository.save(visit));
    }

    @Transactional
    public void deleteVisit(Long id) {
        if (!visitRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Visit with id " + id + " not found");
        }
        visitRepository.deleteById(id);
    }
}