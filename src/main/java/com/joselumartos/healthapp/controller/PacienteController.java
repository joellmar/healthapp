package com.joselumartos.healthapp.controller;

import com.joselumartos.healthapp.model.Paciente;
import com.joselumartos.healthapp.service.PacienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/paciente")
public class PacienteController {
    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping("/perfil")
    public String verPerfil(Principal principal, Model model) {
        String email = principal.getName();
        Paciente paciente = pacienteService.obtenerPorUsuarioId(email);
        model.addAttribute("paciente", paciente);
        return "perfil";
    }

    @GetMapping("/editar")
    public String editarPerfilForm(Principal principal, Model model) {
        Paciente paciente = pacienteService.obtenerPorUsuarioId(principal.getName());
        model.addAttribute("nombre", paciente.getNombre());
        return "editar-perfil";
    }

    @PostMapping("/editar")
    public String procesarEdicion(Principal principal, @RequestParam("nombre") String nombre) {
        pacienteService.actualizarDatosPersonales(principal.getName(), nombre);
        return "redirect:/paciente/perfil?exito";
    }
}