package com.medicalrecords.medical_records.service;

import com.medicalrecords.medical_records.dto.request.CreateDiagnosisRequest;
import com.medicalrecords.medical_records.dto.response.DiagnosisResponse;
import com.medicalrecords.medical_records.entity.Diagnosis;
import com.medicalrecords.medical_records.exception.DuplicateResourceException;
import com.medicalrecords.medical_records.exception.ResourceNotFoundException;
import com.medicalrecords.medical_records.mapper.EntityMapper;
import com.medicalrecords.medical_records.repository.DiagnosisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final EntityMapper mapper;

    public List<DiagnosisResponse> getAllDiagnoses() {
        return diagnosisRepository.findAll()
                .stream()
                .map(mapper::toDiagnosisResponse)
                .toList();
    }

    public DiagnosisResponse getDiagnosisById(Long id) {
        return mapper.toDiagnosisResponse(
                diagnosisRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Diagnosis with id " + id + " not found")));
    }

    @Transactional
    public DiagnosisResponse createDiagnosis(CreateDiagnosisRequest request) {
        if (diagnosisRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException(
                    "Diagnosis '" + request.getName() + "' already exists");
        }

        Diagnosis diagnosis = Diagnosis.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return mapper.toDiagnosisResponse(diagnosisRepository.save(diagnosis));
    }

    @Transactional
    public DiagnosisResponse updateDiagnosis(Long id, CreateDiagnosisRequest request) {
        Diagnosis diagnosis = diagnosisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Diagnosis with id " + id + " not found"));

        diagnosis.setName(request.getName());
        diagnosis.setDescription(request.getDescription());

        return mapper.toDiagnosisResponse(diagnosisRepository.save(diagnosis));
    }

    @Transactional
    public void deleteDiagnosis(Long id) {
        if (!diagnosisRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Diagnosis with id " + id + " not found");
        }
        diagnosisRepository.deleteById(id);
    }
}