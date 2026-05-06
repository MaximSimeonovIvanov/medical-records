package com.medicalrecords.medical_records.controller.web;

import com.medicalrecords.medical_records.dto.request.CreateSickLeaveRequest;
import com.medicalrecords.medical_records.entity.User;
import com.medicalrecords.medical_records.service.SickLeaveService;
import com.medicalrecords.medical_records.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sick-leaves")
@RequiredArgsConstructor
public class SickLeaveWebController {

    private final SickLeaveService sickLeaveService;
    private final VisitService visitService;

    @GetMapping
    public String listSickLeaves(
            @AuthenticationPrincipal User currentUser,
            Model model) {

        if (currentUser.getRole().name().equals("ADMIN")) {
            model.addAttribute("sickLeaves",
                    sickLeaveService.getAllSickLeaves());
        } else if (currentUser.getRole().name().equals("DOCTOR")) {
            model.addAttribute("sickLeaves",
                    sickLeaveService.getSickLeavesByDoctor(
                            currentUser.getDoctor().getId()));
        } else {
            model.addAttribute("sickLeaves",
                    sickLeaveService.getSickLeavesByPatient(
                            currentUser.getPatient().getId()));
        }
        return "sick-leaves/list";
    }

    @GetMapping("/new")
    public String newSickLeaveForm(
            @AuthenticationPrincipal User currentUser,
            Model model) {

        if (currentUser.getRole().name().equals("ADMIN")) {
            //админа вижда вс посещ
            model.addAttribute("visits",
                    visitService.getAllVisits());
        } else {
            //лекар вижда само своите посещ
            model.addAttribute("visits",
                    visitService.getVisitsByDoctor(
                            currentUser.getDoctor().getId()));
        }
        return "sick-leaves/form";
    }

    @PostMapping("/new")
    public String createSickLeave(
            @ModelAttribute CreateSickLeaveRequest request,
            Model model) {
        try {
            sickLeaveService.createSickLeave(request);
            return "redirect:/sick-leaves";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("visits",
                    visitService.getAllVisits());
            return "sick-leaves/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editSickLeaveForm(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser,
            Model model) {
        model.addAttribute("sickLeave",
                sickLeaveService.getSickLeaveById(id));

        if (currentUser.getRole().name().equals("ADMIN")) {
            model.addAttribute("visits",
                    visitService.getAllVisits());
        } else {
            model.addAttribute("visits",
                    visitService.getVisitsByDoctor(
                            currentUser.getDoctor().getId()));
        }
        model.addAttribute("id", id);
        return "sick-leaves/form";
    }

    @PostMapping("/{id}/edit")
    public String updateSickLeave(
            @PathVariable Long id,
            @ModelAttribute CreateSickLeaveRequest request,
            Model model) {
        try {
            sickLeaveService.updateSickLeave(id, request);
            return "redirect:/sick-leaves";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("visits",
                    visitService.getAllVisits());
            return "sick-leaves/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteSickLeave(@PathVariable Long id) {
        sickLeaveService.deleteSickLeave(id);
        return "redirect:/sick-leaves";
    }
}