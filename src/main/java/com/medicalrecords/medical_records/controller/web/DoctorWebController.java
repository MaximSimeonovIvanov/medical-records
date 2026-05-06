package com.medicalrecords.medical_records.controller.web;

import com.medicalrecords.medical_records.dto.request.CreateDoctorRequest;
import com.medicalrecords.medical_records.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorWebController {

    private final DoctorService doctorService;

    @GetMapping
    public String listDoctors(Model model) {
        model.addAttribute("doctors",
                doctorService.getAllDoctors());
        return "doctors/list";
    }

    @GetMapping("/new")
    public String newDoctorForm(Model model) {
        model.addAttribute("doctor",
                new CreateDoctorRequest());
        return "doctors/form";
    }

    @PostMapping("/new")
    public String createDoctor(
            @ModelAttribute CreateDoctorRequest request,
            Model model) {
        try {
            doctorService.createDoctor(request);
            return "redirect:/doctors";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("doctor", request);
            return "doctors/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editDoctorForm(
            @PathVariable Long id, Model model) {
        model.addAttribute("doctor",
                doctorService.getDoctorById(id));
        model.addAttribute("id", id);
        return "doctors/form";
    }

    @PostMapping("/{id}/edit")
    public String updateDoctor(
            @PathVariable Long id,
            @ModelAttribute CreateDoctorRequest request,
            Model model) {
        try {
            doctorService.updateDoctor(id, request);
            return "redirect:/doctors";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "doctors/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return "redirect:/doctors";
    }
}