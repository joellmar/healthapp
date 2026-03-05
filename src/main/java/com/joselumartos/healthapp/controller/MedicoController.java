package com.joselumartos.healthapp.controller;

import com.joselumartos.healthapp.model.Medico;
import com.joselumartos.healthapp.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/medico")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping("/agenda")
    public String verAgenda(Principal principal, Model model) {
        String email = principal.getName(); // Email del token JWT
        Medico medico = medicoService.obtenerPorUsuarioId(email);

        model.addAttribute("medico", medico);
        model.addAttribute("citas", medico.getCitas());
        return "agenda";
    }
}
