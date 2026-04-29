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

    @Query("""
        SELECT MONTH(s.startDate), YEAR(s.startDate), COUNT(s)
        FROM SickLeave s
        GROUP BY MONTH(s.startDate), YEAR(s.startDate)
        ORDER BY COUNT(s) DESC
        """)
    List<Object[]> findMonthWithMostSickLeaves();
}