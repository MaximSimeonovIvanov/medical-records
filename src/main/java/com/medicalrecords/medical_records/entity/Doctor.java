package com.medicalrecords.medical_records.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity

@Table(name = "doctors")

@Data

@NoArgsConstructor

@AllArgsConstructor

@Builder
public class Doctor {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uin", unique = true, nullable = false)
    @NotBlank(message = "UIN is required")
    private String uin;

    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Specialty is required")
    private String specialty;

    @Column(name = "is_gp")
    private boolean gp;

    @OneToMany(mappedBy = "generalPractitioner")
    private List<Patient> patients;

    @OneToMany(mappedBy = "doctor")
    private List<Visit> visits;
}