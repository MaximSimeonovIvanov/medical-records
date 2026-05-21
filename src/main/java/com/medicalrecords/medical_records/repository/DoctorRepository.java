package com.medicalrecords.medical_records.repository;

import com.medicalrecords.medical_records.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByUin(String uin);
}