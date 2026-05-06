package com.medicalrecords.medical_records.controller.web;

import com.medicalrecords.medical_records.repository.*;
import lombok.RequiredArgsConstructor;
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
    public String dashboard(Model model) {
        model.addAttribute("doctorCount",
                doctorRepository.count());
        model.addAttribute("patientCount",
                patientRepository.count());
        model.addAttribute("visitCount",
                visitRepository.count());
        model.addAttribute("sickLeaveCount",
                sickLeaveRepository.count());
        //модела дава данни на таймлийф шаблона в dashborad.html: th:text="${doctorCount}"
        return "dashboard";
    }
}