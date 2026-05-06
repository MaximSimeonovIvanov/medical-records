package com.medicalrecords.medical_records.service;

import com.medicalrecords.medical_records.dto.request.CreateSickLeaveRequest;
import com.medicalrecords.medical_records.dto.response.SickLeaveResponse;
import com.medicalrecords.medical_records.entity.SickLeave;
import com.medicalrecords.medical_records.entity.Visit;
import com.medicalrecords.medical_records.exception.ResourceNotFoundException;
import com.medicalrecords.medical_records.mapper.EntityMapper;
import com.medicalrecords.medical_records.repository.SickLeaveRepository;
import com.medicalrecords.medical_records.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SickLeaveService {

    private final SickLeaveRepository sickLeaveRepository;
    private final VisitRepository visitRepository;
    private final EntityMapper mapper;

    public List<SickLeaveResponse> getAllSickLeaves() {
        return sickLeaveRepository.findAll()
                .stream()
                .map(mapper::toSickLeaveResponse)
                .toList();
    }

    public SickLeaveResponse getSickLeaveById(Long id) {
        return mapper.toSickLeaveResponse(
                sickLeaveRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Sick leave with id " + id + " not found")));
    }

    public List<SickLeaveResponse> getSickLeavesByPatient(Long patientId) {
        return sickLeaveRepository.findByVisitPatientId(patientId)
                .stream()
                .map(mapper::toSickLeaveResponse)
                .toList();
    }

    @Transactional
    public SickLeaveResponse createSickLeave(CreateSickLeaveRequest request) {
        Visit visit = visitRepository.findById(request.getVisitId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Visit with id " + request.getVisitId() + " not found"));

        SickLeave sickLeave = SickLeave.builder()
                .startDate(request.getStartDate())
                .numberOfDays(request.getNumberOfDays())
                .visit(visit)
                .build();

        return mapper.toSickLeaveResponse(sickLeaveRepository.save(sickLeave));
    }

    @Transactional
    public SickLeaveResponse updateSickLeave(Long id, CreateSickLeaveRequest request) {
        SickLeave sickLeave = sickLeaveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sick leave with id " + id + " not found"));

        sickLeave.setStartDate(request.getStartDate());
        sickLeave.setNumberOfDays(request.getNumberOfDays());

        return mapper.toSickLeaveResponse(sickLeaveRepository.save(sickLeave));
    }

    @Transactional
    public void deleteSickLeave(Long id) {
        if (!sickLeaveRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Sick leave with id " + id + " not found");
        }
        sickLeaveRepository.deleteById(id);
    }

    public List<SickLeaveResponse> getSickLeavesByDoctor(Long doctorId) {
        return sickLeaveRepository.findByVisitDoctorId(doctorId)
                .stream()
                .map(mapper::toSickLeaveResponse)
                .toList();
    }
}