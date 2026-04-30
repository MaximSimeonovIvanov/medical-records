package com.medicalrecords.medical_records.repository;

import com.medicalrecords.medical_records.entity.SickLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SickLeaveRepository extends JpaRepository<SickLeave, Long> {

    List<SickLeave> findByVisitPatientId(Long patientId);
    //findBy + Visit + Patient + Id
    //спринг навигира между отношенията sick_leave -> visit -> patient -> id

    List<SickLeave> findByVisitDoctorId(Long doctorId);
    //болични издадени от даден лекар

    @Query(value = """
        SELECT EXTRACT(YEAR FROM start_date) as year,
               EXTRACT(MONTH FROM start_date) as month,
               COUNT(*) as total
        FROM sick_leaves
        GROUP BY year, month
        ORDER BY total DESC
        LIMIT 1
        """, nativeQuery = true)
    List<Object[]> findMonthWithMostSickLeaves();

    @Query("""
        SELECT s.visit.doctor.name, COUNT(s) as total
        FROM SickLeave s
        GROUP BY s.visit.doctor.name
        ORDER BY total DESC
        """)
    List<Object[]> findDoctorWithMostSickLeaves();
}