package com.medicalrecords.medical_records.controller.web;

import com.medicalrecords.medical_records.dto.request.CreatePatientRequest;
import com.medicalrecords.medical_records.service.DoctorService;
import com.medicalrecords.medical_records.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientWebController {

    private final PatientService patientService;
    private final DoctorService doctorService;

    @GetMapping
    public String listPatients(Model model) {
        model.addAttribute("patients",
                patientService.getAllPatients());
        return "patients/list";
    }

    @GetMapping("/new")
    public String newPatientForm(Model model) {
        model.addAttribute("patient",
                new CreatePatientRequest());
        model.addAttribute("gps",
                doctorService.getAllGPs());
        //подавам лист от ГПта за да покаже дропдаун меню
        return "patients/form";
    }

    @PostMapping("/new")
    public String createPatient(
            @ModelAttribute CreatePatientRequest request,
            Model model) {
        try {
            patientService.createPatient(request);
            return "redirect:/patients";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("gps", doctorService.getAllGPs());
            model.addAttribute("patient", request);
            return "patients/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editPatientForm(
            @PathVariable Long id, Model model) {
        model.addAttribute("patient",
                patientService.getPatientById(id));
        model.addAttribute("gps", doctorService.getAllGPs());
        model.addAttribute("id", id);
        return "patients/form";
    }

    @PostMapping("/{id}/edit")
    public String updatePatient(
            @PathVariable Long id,
            @ModelAttribute CreatePatientRequest request,
            Model model) {
        try {
            patientService.updatePatient(id, request);
            return "redirect:/patients";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("gps", doctorService.getAllGPs());
            return "patients/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return "redirect:/patients";
    }
}