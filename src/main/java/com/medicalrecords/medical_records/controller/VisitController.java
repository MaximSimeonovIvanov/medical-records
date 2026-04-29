package com.medicalrecords.medical_records.controller;

import com.medicalrecords.medical_records.dto.request.CreateVisitRequest;
import com.medicalrecords.medical_records.dto.response.VisitResponse;
import com.medicalrecords.medical_records.service.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @GetMapping
    public ResponseEntity<List<VisitResponse>> getAllVisits() {
        return ResponseEntity.ok(visitService.getAllVisits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitResponse> getVisitById(@PathVariable Long id) {
        return ResponseEntity.ok(visitService.getVisitById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<VisitResponse>> getVisitsByPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(visitService.getVisitsByPatient(patientId));
        //връщам посещенията за пациент с id 3
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<VisitResponse>> getVisitsByDoctor(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(visitService.getVisitsByDoctor(doctorId));
    }

    @GetMapping("/doctor/{doctorId}/period")
    public ResponseEntity<List<VisitResponse>> getVisitsByDoctorAndPeriod(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(
                visitService.getVisitsByDoctorAndPeriod(doctorId, from, to));
        //връщам посещенията на даден лекар в даден период
        //requestparam чете query параметри от url
    }

    @PostMapping
    public ResponseEntity<VisitResponse> createVisit(
            @Valid @RequestBody CreateVisitRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(visitService.createVisit(request, null));
        //за сега не слагам потр име, тодо
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitResponse> updateVisit(
            @PathVariable Long id,
            @Valid @RequestBody CreateVisitRequest request) {
        return ResponseEntity.ok(visitService.updateVisit(id, request, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisit(@PathVariable Long id) {
        visitService.deleteVisit(id);
        return ResponseEntity.noContent().build();
    }
}