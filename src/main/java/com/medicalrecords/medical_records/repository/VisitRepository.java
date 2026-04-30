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

    @Query("""
        SELECT v.diagnosis.name, COUNT(v) as total
        FROM Visit v
        WHERE v.diagnosis IS NOT NULL
        GROUP BY v.diagnosis.name
        ORDER BY total DESC
        """)
    List<Object[]> findDiagnosisFrequency();
    //връщам лист от двойки име_диагноза, брой - подредени по честота

    @Query("""
        SELECT SUM(v.price)
        FROM Visit v
        WHERE v.paidByNhif = false
        """)
    java.math.BigDecimal getTotalPaidByPatients();

    @Query("""
        SELECT v.doctor.name, SUM(v.price) as total
        FROM Visit v
        WHERE v.paidByNhif = false
        GROUP BY v.doctor.name
        ORDER BY total DESC
        """)
    List<Object[]> getTotalPaidByPatientsPerDoctor();

    @Query("""
        SELECT v.doctor.name, COUNT(v) as total
        FROM Visit v
        GROUP BY v.doctor.name
        ORDER BY total DESC
        """)
    List<Object[]> getVisitCountPerDoctor();
}