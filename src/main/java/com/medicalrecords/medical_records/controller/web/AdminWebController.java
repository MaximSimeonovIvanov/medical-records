package com.medicalrecords.medical_records.controller.web;

import com.medicalrecords.medical_records.entity.User;
import com.medicalrecords.medical_records.repository.UserRepository;
import com.medicalrecords.medical_records.repository.DoctorRepository;
import com.medicalrecords.medical_records.repository.PatientRepository;
import com.medicalrecords.medical_records.repository.VisitRepository;
import com.medicalrecords.medical_records.repository.SickLeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminWebController {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;
    private final SickLeaveRepository sickLeaveRepository;

    @GetMapping
    public String adminPanel(Model model) {
        // System overview counts
        model.addAttribute("totalUsers",
                userRepository.count());
        model.addAttribute("totalDoctors",
                doctorRepository.count());
        model.addAttribute("totalPatients",
                patientRepository.count());
        model.addAttribute("totalVisits",
                visitRepository.count());
        model.addAttribute("totalSickLeaves",
                sickLeaveRepository.count());

        //list ot vsichki portebiteli
        model.addAttribute("users",
                userRepository.findAll());

        return "admin/panel";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }
}