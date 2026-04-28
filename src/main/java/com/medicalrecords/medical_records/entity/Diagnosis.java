package com.medicalrecords.medical_records.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "diagnoses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Diagnosis name is required")
    private String name;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "diagnosis")
    //edna diagnoza moje da q sreshtnem v mn poseshteniya
    private List<Visit> visits;
}