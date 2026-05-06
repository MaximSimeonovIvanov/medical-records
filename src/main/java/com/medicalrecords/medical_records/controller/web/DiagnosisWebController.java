package com.medicalrecords.medical_records.controller.web;

import com.medicalrecords.medical_records.dto.request.CreateDiagnosisRequest;
import com.medicalrecords.medical_records.service.DiagnosisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/diagnoses")
@RequiredArgsConstructor
public class DiagnosisWebController {

    private final DiagnosisService diagnosisService;

    @GetMapping
    public String listDiagnoses(Model model) {
        model.addAttribute("diagnoses",
                diagnosisService.getAllDiagnoses());
        return "diagnoses/list";
    }

    @GetMapping("/new")
    public String newDiagnosisForm(Model model) {
        model.addAttribute("diagnosis",
                new CreateDiagnosisRequest());
        return "diagnoses/form";
    }

    @PostMapping("/new")
    public String createDiagnosis(
            @ModelAttribute CreateDiagnosisRequest request,
            Model model) {
        try {
            diagnosisService.createDiagnosis(request);
            return "redirect:/diagnoses";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("diagnosis", request);
            return "diagnoses/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editDiagnosisForm(
            @PathVariable Long id, Model model) {
        model.addAttribute("diagnosis",
                diagnosisService.getDiagnosisById(id));
        model.addAttribute("id", id);
        return "diagnoses/form";
    }

    @PostMapping("/{id}/edit")
    public String updateDiagnosis(
            @PathVariable Long id,
            @ModelAttribute CreateDiagnosisRequest request,
            Model model) {
        try {
            diagnosisService.updateDiagnosis(id, request);
            return "redirect:/diagnoses";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "diagnoses/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteDiagnosis(@PathVariable Long id) {
        diagnosisService.deleteDiagnosis(id);
        return "redirect:/diagnoses";
    }
}