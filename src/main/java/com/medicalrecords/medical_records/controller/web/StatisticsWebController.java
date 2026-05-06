package com.medicalrecords.medical_records.controller.web;

import com.medicalrecords.medical_records.service.StatisticsService;
import com.medicalrecords.medical_records.service.DiagnosisService;
import com.medicalrecords.medical_records.service.DoctorService;
import com.medicalrecords.medical_records.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsWebController {

    private final StatisticsService statisticsService;
    private final DiagnosisService diagnosisService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @GetMapping
    public String statisticsDashboard(
            @RequestParam(required = false) Long diagnosisId,
            @RequestParam(required = false) Long gpId,
            @RequestParam(required = false) Long patientId,
            Model model) {

        //диагнози
        model.addAttribute("mostCommonDiagnosis",
                statisticsService.getMostCommonDiagnosis());
        model.addAttribute("diagnosisFrequency",
                statisticsService.getDiagnosisFrequency());

        //финанси
        model.addAttribute("totalPaidByPatients",
                statisticsService.getTotalPaidByPatients());
        model.addAttribute("paidPerDoctor",
                statisticsService.getPaidByPatientsPerDoctor());

        //лекарски
        model.addAttribute("visitCountPerDoctor",
                statisticsService.getVisitCountPerDoctor());
        model.addAttribute("doctorsWithMostSickLeaves",
                statisticsService.getDoctorsWithMostSickLeaves());

        //ЛЛ
        model.addAttribute("patientCountPerGP",
                statisticsService.getPatientCountPerGP());

        //болнични
        model.addAttribute("monthWithMostSickLeaves",
                statisticsService.getMonthWithMostSickLeaves());

        //филтър дропдаун
        model.addAttribute("allDiagnoses",
                diagnosisService.getAllDiagnoses());
        model.addAttribute("allGPs",
                doctorService.getAllGPs());
        model.addAttribute("allPatients",
                patientService.getAllPatients());

        //филтрирам резултати когато има използван филтър
        if (diagnosisId != null) {
            model.addAttribute("patientsByDiagnosis",
                    statisticsService.getPatientsByDiagnosis(diagnosisId));
            model.addAttribute("selectedDiagnosisId", diagnosisId);
        }

        if (gpId != null) {
            model.addAttribute("patientsByGP",
                    statisticsService.getPatientsByGP(gpId));
            model.addAttribute("selectedGpId", gpId);
        }

        if (patientId != null) {
            model.addAttribute("patientHistory",
                    statisticsService.getPatientHistory(patientId));
            model.addAttribute("selectedPatientId", patientId);
        }

        return "statistics/dashboard";
    }
}