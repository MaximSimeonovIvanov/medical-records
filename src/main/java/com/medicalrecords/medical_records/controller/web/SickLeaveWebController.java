package com.medicalrecords.medical_records.controller.web;

import com.medicalrecords.medical_records.dto.request.CreateSickLeaveRequest;
import com.medicalrecords.medical_records.service.SickLeaveService;
import com.medicalrecords.medical_records.service.VisitService;
import lombok.RequiredArgsConstructor;
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
    public String listSickLeaves(Model model) {
        model.addAttribute("sickLeaves",
                sickLeaveService.getAllSickLeaves());
        return "sick-leaves/list";
    }

    @GetMapping("/new")
    public String newSickLeaveForm(Model model) {
        model.addAttribute("sickLeave",
                new CreateSickLeaveRequest());
        model.addAttribute("visits",
                visitService.getAllVisits());
        // Pass all visits so doctor can pick which
        // visit this sick leave belongs to
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
            @PathVariable Long id, Model model) {
        model.addAttribute("sickLeave",
                sickLeaveService.getSickLeaveById(id));
        model.addAttribute("visits",
                visitService.getAllVisits());
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