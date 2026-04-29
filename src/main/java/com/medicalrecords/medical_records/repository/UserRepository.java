package com.medicalrecords.medical_records.repository;

import com.medicalrecords.medical_records.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    //спринг секюрити зарежда потр. по време на логин

    boolean existsByUsername(String username);
}