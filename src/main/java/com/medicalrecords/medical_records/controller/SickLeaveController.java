package com.medicalrecords.medical_records.controller;

import com.medicalrecords.medical_records.dto.request.CreateSickLeaveRequest;
import com.medicalrecords.medical_records.dto.response.SickLeaveResponse;
import com.medicalrecords.medical_records.service.SickLeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sick-leaves")
@RequiredArgsConstructor
public class SickLeaveController {

    private final SickLeaveService sickLeaveService;

    @GetMapping
    public ResponseEntity<List<SickLeaveResponse>> getAllSickLeaves() {
        return ResponseEntity.ok(sickLeaveService.getAllSickLeaves());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SickLeaveResponse> getSickLeaveById(@PathVariable Long id) {
        return ResponseEntity.ok(sickLeaveService.getSickLeaveById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<SickLeaveResponse>> getSickLeavesByPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(
                sickLeaveService.getSickLeavesByPatient(patientId));
    }

    @PostMapping
    public ResponseEntity<SickLeaveResponse> createSickLeave(
            @Valid @RequestBody CreateSickLeaveRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(sickLeaveService.createSickLeave(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SickLeaveResponse> updateSickLeave(
            @PathVariable Long id,
            @Valid @RequestBody CreateSickLeaveRequest request) {
        return ResponseEntity.ok(sickLeaveService.updateSickLeave(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSickLeave(@PathVariable Long id) {
        sickLeaveService.deleteSickLeave(id);
        return ResponseEntity.noContent().build();
    }
}