package com.medicalrecords.medical_records.controller;

import com.medicalrecords.medical_records.dto.request.CreateDiagnosisRequest;
import com.medicalrecords.medical_records.dto.response.DiagnosisResponse;
import com.medicalrecords.medical_records.service.DiagnosisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diagnoses")
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    @GetMapping
    public ResponseEntity<List<DiagnosisResponse>> getAllDiagnoses() {
        return ResponseEntity.ok(diagnosisService.getAllDiagnoses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiagnosisResponse> getDiagnosisById(@PathVariable Long id) {
        return ResponseEntity.ok(diagnosisService.getDiagnosisById(id));
    }

    @PostMapping
    public ResponseEntity<DiagnosisResponse> createDiagnosis(
            @Valid @RequestBody CreateDiagnosisRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(diagnosisService.createDiagnosis(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiagnosisResponse> updateDiagnosis(
            @PathVariable Long id,
            @Valid @RequestBody CreateDiagnosisRequest request) {
        return ResponseEntity.ok(diagnosisService.updateDiagnosis(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiagnosis(@PathVariable Long id) {
        diagnosisService.deleteDiagnosis(id);
        return ResponseEntity.noContent().build();
    }
}