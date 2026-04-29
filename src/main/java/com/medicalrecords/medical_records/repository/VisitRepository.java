package com.medicalrecords.medical_records.repository;

import com.medicalrecords.medical_records.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    List<Visit> findByPatientId(Long patientId);
    //всички посещ. на пациента

    List<Visit> findByDoctorId(Long doctorId);

    List<Visit> findByDoctorIdAndDateBetween(Long doctorId, LocalDate from, LocalDate to);

    List<Visit> findByDiagnosisId(Long diagnosisId);

    @Query("SELECT v FROM Visit v WHERE v.patient.id = :patientId ORDER BY v.date DESC")
    List<Visit> findPatientHistory(Long patientId);

    @Query("SELECT SUM(v.price) FROM Visit v WHERE v.paidByNhif = false")
    java.math.BigDecimal getTotalPaidByPatients();
}