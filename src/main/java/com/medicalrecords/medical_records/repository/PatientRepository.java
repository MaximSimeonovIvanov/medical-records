package com.medicalrecords.medical_records.repository;

import com.medicalrecords.medical_records.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByEgn(String egn);
    // SELECT * FROM patients WHERE egn = ?

    boolean existsByEgn(String egn);

    List<Patient> findByGeneralPractitionerId(Long gpId);
    //връща пациенти свързани към дадено GP
}