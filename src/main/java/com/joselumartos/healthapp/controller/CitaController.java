package com.joselumartos.healthapp.controller;

import com.joselumartos.healthapp.dto.CitaDTO;
import com.joselumartos.healthapp.dto.CitaMapper;
import com.joselumartos.healthapp.exception.CitaNoEncontradaException;
import com.joselumartos.healthapp.model.CitaMedica;
import com.joselumartos.healthapp.model.Paciente;
import com.joselumartos.healthapp.service.CitaService;
import com.joselumartos.healthapp.service.MedicoService;
import com.joselumartos.healthapp.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/citas")
public class CitaController {

    private final PacienteService pacienteService;
    private final CitaService citaService;
    private final CitaMapper citaMapper;
    private final MedicoService medicoService;

    public CitaController(PacienteService pacienteService, CitaService citaService, CitaMapper citaMapper, MedicoService medicoService) {
        this.pacienteService = pacienteService;
        this.citaService = citaService;
        this.citaMapper = citaMapper;
        this.medicoService = medicoService;
    }

//    @GetMapping("/mis-citas")
//    public String listarMisCitas(Principal principal, Model model) {
//        Paciente paciente = pacienteService.obtenerPorUsuarioId(principal.getName());
//        model.addAttribute("citas", paciente.getCitas());
//        return "mis-citas";
//    }

    @GetMapping("/mis-citas")
    public String listarMisCitas(Principal principal, Model model,
                                 @RequestParam(required = false) String estadoFiltro,
                                 @RequestParam(required = false) String fechaFiltro) { // El HTML type="date" manda "YYYY-MM-DD"

        Paciente paciente = pacienteService.obtenerPorUsuarioId(principal.getName());
        List<CitaMedica> citas = paciente.getCitas();

        if (estadoFiltro != null && !estadoFiltro.isEmpty()) {
            citas = citas.stream().filter(c -> c.getEstado().name().equals(estadoFiltro)).toList();
        }
        if (fechaFiltro != null && !fechaFiltro.isEmpty()) {
            LocalDate fechaBuscada = LocalDate.parse(fechaFiltro);
            citas = citas.stream().filter(c -> c.getFecha().equals(fechaBuscada)).toList();
        }

        model.addAttribute("citas", citas);
        return "mis-citas";
    }


    @GetMapping("/nueva")
    public String nuevaCitaForm(Model model) {
        // Pasamos un DTO vacío y la lista de TODOS los médicos para el `<select>`
        model.addAttribute("citaDTO", new CitaDTO(null, null, null, null, null, null));
        model.addAttribute("medicos", medicoService.obtenerTodos());
        return "nueva-cita";
    }

    @PostMapping("/nueva")
    public String guardarCita(@ModelAttribute CitaDTO citaDTO, Principal principal) {
        citaService.crearCita(principal.getName(), citaDTO);
        return "redirect:/citas/mis-citas";
    }

    @PostMapping("/cancelar/{id}")
    public String cancelarCita(@PathVariable String id, Authentication authentication) {
        String email = authentication.getName();

        // Extraemos el rol (suponiendo que solo tiene uno)
        String rol = authentication.getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow()
                .getAuthority();

        citaService.cancelarCita(id, email, rol);
        return "redirect:/citas/mis-citas";
    }

    @GetMapping("/detalle/{id}")
    public String verDetalleCita(@PathVariable String id, Principal principal, Model model) {
        // Buscamos la cita del paciente
        Paciente p = pacienteService.obtenerPorUsuarioId(principal.getName());
        CitaMedica cita = p.getCitas().stream().filter(c -> c.getId().equals(id))
                .findFirst().orElseThrow(() -> new CitaNoEncontradaException("Cita no encontrada"));

        model.addAttribute("cita", cita);
        return "detalle-cita";
    }

    @GetMapping("/editar/{id}")
    public String editarCitaForm(@PathVariable String id, Principal principal, Model model) {
        Paciente p = pacienteService.obtenerPorUsuarioId(principal.getName());
        CitaMedica cita = p.getCitas().stream().filter(c -> c.getId().equals(id)).findFirst().orElseThrow();

        model.addAttribute("citaDTO", citaMapper.toDTO(cita));
        model.addAttribute("medicos", medicoService.obtenerTodos()); // Para volver a elegir
        return "editar-cita"; // Es casi igual que nueva-cita.html, pero la ruta POST es distinta
    }

    @PostMapping("/editar/{id}")
    public String procesarEdicionCita(@PathVariable String id, @Valid @ModelAttribute CitaDTO citaDTO, Principal principal) {
        citaService.editarCitaSegura(id, citaDTO, principal.getName());
        return "redirect:/citas/mis-citas";
    }
}
