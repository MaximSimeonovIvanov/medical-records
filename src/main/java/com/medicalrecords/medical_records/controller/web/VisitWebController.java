package com.medicalrecords.medical_records.controller.web;

import com.medicalrecords.medical_records.dto.request.CreateVisitRequest;
import com.medicalrecords.medical_records.dto.response.VisitResponse;
import com.medicalrecords.medical_records.entity.User;
import com.medicalrecords.medical_records.service.DiagnosisService;
import com.medicalrecords.medical_records.service.DoctorService;
import com.medicalrecords.medical_records.service.PatientService;
import com.medicalrecords.medical_records.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitWebController {

    private final VisitService visitService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final DiagnosisService diagnosisService;

    @GetMapping
    public String listVisits(
            @AuthenticationPrincipal User currentUser,
            Model model) {
        List<VisitResponse> visits;

        if (currentUser.getRole().name().equals("ADMIN")) {
            visits = visitService.getAllVisits();
            //админът вижда вс посещ
        } else if (currentUser.getRole().name().equals("DOCTOR")) {
            visits = visitService.getVisitsByDoctor(
                    currentUser.getDoctor().getId());
            //лекарят вижда само своите посещ
        } else {
            visits = visitService.getVisitsByPatient(
                    currentUser.getPatient().getId());
            //пац виждда само своите
        }

        model.addAttribute("visits", visits);
        model.addAttribute("currentUser", currentUser);
        return "visits/list";
    }

    @GetMapping("/new")
    public String newVisitForm(
            @AuthenticationPrincipal User currentUser,
            Model model) {
        model.addAttribute("visit", new CreateVisitRequest());
        model.addAttribute("patients",
                patientService.getAllPatients());
        model.addAttribute("diagnoses",
                diagnosisService.getAllDiagnoses());
        model.addAttribute("doctors",
                doctorService.getAllDoctors());
        model.addAttribute("currentUser", currentUser);
        return "visits/form";
    }

    @PostMapping("/new")
    public String createVisit(
            @ModelAttribute CreateVisitRequest request,
            @AuthenticationPrincipal User currentUser,
            Model model) {
        try {
            //ако лекарят създ посещ автоматично си възлага посещението на себе си
            if (currentUser.getRole().name().equals("DOCTOR")) {
                visitService.createVisitForDoctor(
                        request, currentUser.getDoctor().getId());
            } else {
                visitService.createVisit(request, null);
            }
            return "redirect:/visits";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("patients",
                    patientService.getAllPatients());
            model.addAttribute("diagnoses",
                    diagnosisService.getAllDiagnoses());
            model.addAttribute("doctors",
                    doctorService.getAllDoctors());
            return "visits/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editVisitForm(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser,
            Model model) {
        VisitResponse visit = visitService.getVisitById(id);
        model.addAttribute("visit", visit);
        model.addAttribute("id", id);
        model.addAttribute("patients",
                patientService.getAllPatients());
        model.addAttribute("diagnoses",
                diagnosisService.getAllDiagnoses());
        model.addAttribute("doctors",
                doctorService.getAllDoctors());
        return "visits/form";
    }

    @PostMapping("/{id}/edit")
    public String updateVisit(
            @PathVariable Long id,
            @ModelAttribute CreateVisitRequest request,
            @AuthenticationPrincipal User currentUser,
            Model model) {
        try {
            visitService.updateVisit(id, request,
                    currentUser.getUsername());
            return "redirect:/visits";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("patients",
                    patientService.getAllPatients());
            model.addAttribute("diagnoses",
                    diagnosisService.getAllDiagnoses());
            model.addAttribute("doctors",
                    doctorService.getAllDoctors());
            return "visits/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteVisit(@PathVariable Long id) {
        visitService.deleteVisit(id);
        return "redirect:/visits";
    }
}