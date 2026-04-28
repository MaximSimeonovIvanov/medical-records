package com.medicalrecords.medical_records.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "sick_leaves")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SickLeave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @Column(name = "number_of_days", nullable = false)
    @Min(value = 1, message = "Sick leave must be at least 1 day")
    private int numberOfDays;

    @OneToOne
    @JoinColumn(name = "visit_id", nullable = false)
    private Visit visit;
}