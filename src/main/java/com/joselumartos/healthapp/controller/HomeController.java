package com.joselumartos.healthapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("email", authentication.getName());
            String rol = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(r -> r.getAuthority())
                    .orElse("ROLE_PACIENTE");
            model.addAttribute("rol", rol);
        }
        return "home";
    }
}