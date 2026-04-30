package com.medicalrecords.medical_records.controller;

import com.medicalrecords.medical_records.dto.response.*;
import com.medicalrecords.medical_records.service.StatisticsService;
import com.medicalrecords.medical_records.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final VisitService visitService;

    @GetMapping("/patients/by-diagnosis/{diagnosisId}")
    public ResponseEntity<List<PatientResponse>> getPatientsByDiagnosis(
            @PathVariable Long diagnosisId) {
        return ResponseEntity.ok(
                statisticsService.getPatientsByDiagnosis(diagnosisId));
    }
    //get /api/statistics/patients/by-diagnosis/1

    @GetMapping("/diagnoses/most-common")
    public ResponseEntity<DiagnosisStatResponse> getMostCommonDiagnosis() {
        return ResponseEntity.ok(
                statisticsService.getMostCommonDiagnosis());
    }
    //get /api/statistics/diagnoses/most-common

    @GetMapping("/diagnoses/frequency")
    public ResponseEntity<List<DiagnosisStatResponse>> getDiagnosisFrequency() {
        return ResponseEntity.ok(
                statisticsService.getDiagnosisFrequency());
    }
    //get /api/statistics/diagnoses/frequency

    @GetMapping("/patients/by-gp/{gpId}")
    public ResponseEntity<List<PatientResponse>> getPatientsByGP(
            @PathVariable Long gpId) {
        return ResponseEntity.ok(
                statisticsService.getPatientsByGP(gpId));
    }
    //get /api/statistics/patients/by-gp/1

    @GetMapping("/visits/total-paid-by-patients")
    public ResponseEntity<BigDecimal> getTotalPaidByPatients() {
        return ResponseEntity.ok(
                statisticsService.getTotalPaidByPatients());
    }
    //get /api/statistics/visits/total-paid-by-patients

    @GetMapping("/visits/paid-by-patients-per-doctor")
    public ResponseEntity<List<DoctorStatResponse>> getPaidByPatientsPerDoctor() {
        return ResponseEntity.ok(
                statisticsService.getPaidByPatientsPerDoctor());
    }
    //get /api/statistics/visits/paid-by-patients-per-doctor

    @GetMapping("/patients/count-per-gp")
    public ResponseEntity<List<DoctorStatResponse>> getPatientCountPerGP() {
        return ResponseEntity.ok(
                statisticsService.getPatientCountPerGP());
    }
    //get /api/statistics/patients/count-per-gp

    @GetMapping("/visits/count-per-doctor")
    public ResponseEntity<List<DoctorStatResponse>> getVisitCountPerDoctor() {
        return ResponseEntity.ok(
                statisticsService.getVisitCountPerDoctor());
    }
    //get /api/statistics/visits/count-per-doctor

    @GetMapping("/visits/patient-history/{patientId}")
    public ResponseEntity<List<VisitResponse>> getPatientHistory(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(
                statisticsService.getPatientHistory(patientId));
    }
    //get /api/statistics/visits/patient-history/1

    @GetMapping("/visits/by-doctor-and-period")
    public ResponseEntity<List<VisitResponse>> getVisitsByDoctorAndPeriod(
            @RequestParam Long doctorId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        //ako nqmam data, za celiq period
        if (from == null) from = LocalDate.of(2000, 1, 1);
        if (to == null) to = LocalDate.now();

        return ResponseEntity.ok(
                visitService.getVisitsByDoctorAndPeriod(doctorId, from, to));
    }
    //get /api/statistics/visits/by-doctor-and-period?doctorId=1
    //get /api/statistics/visits/by-doctor-and-period?doctorId=1&from=2025-01-01&to=2025-12-31

    @GetMapping("/sick-leaves/month-most-issued")
    public ResponseEntity<MonthStatResponse> getMonthWithMostSickLeaves() {
        return ResponseEntity.ok(
                statisticsService.getMonthWithMostSickLeaves());
    }
    //get /api/statistics/sick-leaves/month-most-issued

    @GetMapping("/sick-leaves/doctor-most-issued")
    public ResponseEntity<List<DoctorStatResponse>> getDoctorsWithMostSickLeaves() {
        return ResponseEntity.ok(
                statisticsService.getDoctorsWithMostSickLeaves());
    }
    //get /api/statistics/sick-leaves/doctor-most-issued
}