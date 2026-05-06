package com.medicalrecords.medical_records.controller.web;

import com.medicalrecords.medical_records.dto.request.RegisterRequest;
import com.medicalrecords.medical_records.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
//@controller returns HTML pages, @restcontroller returns JSON
@RequiredArgsConstructor
public class AuthWebController {

    private final AuthService authService;

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
        //redirect root url to dashboard
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
        //returns templates/auth/login.html
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String name,
            @RequestParam String egn,
            Model model) {
        try {
            RegisterRequest request = new RegisterRequest();
            request.setUsername(username);
            request.setPassword(password);
            request.setName(name);
            request.setEgn(egn);
            authService.register(request);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }
}