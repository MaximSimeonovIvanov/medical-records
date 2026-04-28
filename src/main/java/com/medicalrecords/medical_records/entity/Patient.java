package com.medicalrecords.medical_records.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(unique = true, nullable = false, length = 10)
    @NotBlank(message = "EGN is required")
    @Pattern(regexp = "\\d{10}", message = "EGN must be exactly 10 digits")
    // EGN e 10 cifri, validiram s regex
    private String egn;

    @Column(name = "health_insured")
    private boolean healthInsured;

    @ManyToOne
    @JoinColumn(name = "gp_id")
    //създава foreign key в таблицата на пациента, сочещ към таблицата на дадения доктор
    private Doctor generalPractitioner;

    @OneToMany(mappedBy = "patient")
    private List<Visit> visits;
}