package com.medicalrecords.medical_records.controller.web;

import com.medicalrecords.medical_records.entity.User;
import com.medicalrecords.medical_records.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardWebController {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;
    private final SickLeaveRepository sickLeaveRepository;

    @GetMapping("/dashboard")
    public String dashboard(
            @AuthenticationPrincipal User currentUser,
            Model model) {

        if (currentUser.getRole().name().equals("PATIENT")) {
            //пац вижда само собствен лекари
            Long patientId = currentUser.getPatient().getId();
            model.addAttribute("doctorCount",
                    doctorRepository.count());
            model.addAttribute("patientCount", 1);
            //само себе си
            model.addAttribute("visitCount",
                    visitRepository.findByPatientId(patientId).size());
            model.addAttribute("sickLeaveCount",
                    sickLeaveRepository.findByVisitPatientId(patientId).size());
        } else {
            //админ и лекар вижда всичко и всички
            model.addAttribute("doctorCount",
                    doctorRepository.count());
            model.addAttribute("patientCount",
                    patientRepository.count());
            model.addAttribute("visitCount",
                    visitRepository.count());
            model.addAttribute("sickLeaveCount",
                    sickLeaveRepository.count());
        }
        return "dashboard";
    }
}