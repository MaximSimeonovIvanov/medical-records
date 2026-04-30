package com.medicalrecords.medical_records.service;

import com.medicalrecords.medical_records.dto.response.*;
import com.medicalrecords.medical_records.mapper.EntityMapper;
import com.medicalrecords.medical_records.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final SickLeaveRepository sickLeaveRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final EntityMapper mapper;

    public List<PatientResponse> getPatientsByDiagnosis(Long diagnosisId) {
        return visitRepository.findByDiagnosisId(diagnosisId)
                .stream()
                .map(visit -> mapper.toPatientResponse(visit.getPatient()))
                .distinct()
                //distinct премахва дублиращи се пац.
                //един и същ пац. може да има една диагноза мн. пъти
                .toList();
    }

    public DiagnosisStatResponse getMostCommonDiagnosis() {
        List<Object[]> results = visitRepository.findDiagnosisFrequency();
        if (results.isEmpty()) {
            return null;
        }
        Object[] top = results.get(0);
        //първи ред = най-честа
        // top0 = име диагн., top1 = брой
        return new DiagnosisStatResponse(
                (String) top[0],
                ((Number) top[1]).longValue()
        );
    }

    //честота на диагноза
    public List<DiagnosisStatResponse> getDiagnosisFrequency() {
        return visitRepository.findDiagnosisFrequency()
                .stream()
                .map(row -> new DiagnosisStatResponse(
                        (String) row[0],
                        ((Number) row[1]).longValue()
                ))
                .toList();
    }

    public List<PatientResponse> getPatientsByGP(Long gpId) {
        return patientRepository.findByGeneralPractitionerId(gpId)
                .stream()
                .map(mapper::toPatientResponse)
                .toList();
    }

    public BigDecimal getTotalPaidByPatients() {
        BigDecimal total = visitRepository.getTotalPaidByPatients();
        return total != null ? total : BigDecimal.ZERO;
        //няма посещ.=връщам 0 вместо null
    }

    public List<DoctorStatResponse> getPaidByPatientsPerDoctor() {
        return visitRepository.getTotalPaidByPatientsPerDoctor()
                .stream()
                .map(row -> new DoctorStatResponse(
                        (String) row[0],
                        null,
                        // no count for this stat
                        new BigDecimal(row[1].toString())
                ))
                .toList();
    }

    public List<DoctorStatResponse> getPatientCountPerGP() {
        return patientRepository.countPatientsPerGP()
                .stream()
                .map(row -> new DoctorStatResponse(
                        (String) row[0],
                        ((Number) row[1]).longValue(),
                        null
                        // no amount for this stat
                ))
                .toList();
    }

    public List<DoctorStatResponse> getVisitCountPerDoctor() {
        return visitRepository.getVisitCountPerDoctor()
                .stream()
                .map(row -> new DoctorStatResponse(
                        (String) row[0],
                        ((Number) row[1]).longValue(),
                        null
                ))
                .toList();
    }

    public List<VisitResponse> getPatientHistory(Long patientId) {
        return visitRepository.findPatientHistory(patientId)
                .stream()
                .map(mapper::toVisitResponse)
                .toList();
    }

    public MonthStatResponse getMonthWithMostSickLeaves() {
        List<Object[]> results = sickLeaveRepository
                .findMonthWithMostSickLeaves();
        if (results.isEmpty()) {
            return null;
        }
        Object[] top = results.get(0);
        return new MonthStatResponse(
                ((Number) top[0]).intValue(),
                ((Number) top[1]).intValue(),
                ((Number) top[2]).longValue()
        );
    }

    public List<DoctorStatResponse> getDoctorsWithMostSickLeaves() {
        List<Object[]> results = sickLeaveRepository
                .findDoctorWithMostSickLeaves();
        if (results.isEmpty()) {
            return List.of();
        }

        //вземам макса от първия резулт
        long maxCount = ((Number) results.get(0)[1]).longValue();

        //връщам вс. лекари с еднакъв максимум; може да има равни
        return results.stream()
                .filter(row -> ((Number) row[1]).longValue() == maxCount)
                .map(row -> new DoctorStatResponse(
                        (String) row[0],
                        ((Number) row[1]).longValue(),
                        null
                ))
                .toList();
    }
}