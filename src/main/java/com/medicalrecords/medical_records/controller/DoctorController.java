package com.medicalrecords.medical_records.controller;

import com.medicalrecords.medical_records.dto.request.CreateDoctorRequest;
import com.medicalrecords.medical_records.dto.response.DoctorResponse;
import com.medicalrecords.medical_records.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
        //                        HTTP 200 OK + the data
        //tova se vrashta kam klienta JSON format
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
        // get /api/doctors/5 -> id = 5
    }

    @GetMapping("/gps")
    public ResponseEntity<List<DoctorResponse>> getAllGPs() {
        return ResponseEntity.ok(doctorService.getAllGPs());
        //връща само GPтата
    }

    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(
            @Valid @RequestBody CreateDoctorRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)  // HTTP 201 Created
                .body(doctorService.createDoctor(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody CreateDoctorRequest request) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
        // http 204 no content — успех, няма нищо за връщане
    }
}