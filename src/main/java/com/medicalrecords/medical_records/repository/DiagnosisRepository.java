package com.medicalrecords.medical_records.repository;

import com.medicalrecords.medical_records.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    boolean existsByName(String name);
}