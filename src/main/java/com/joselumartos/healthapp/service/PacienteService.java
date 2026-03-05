package com.joselumartos.healthapp.service;

import com.joselumartos.healthapp.exception.PerfilNoEncontradoException;
import com.joselumartos.healthapp.model.Paciente;
import com.joselumartos.healthapp.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PacienteService {
    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public void crearPerfilPaciente(String email, String nombre) {
        Paciente nuevoPaciente = new Paciente();
        nuevoPaciente.setUsuarioId(email); // Esta es nuestra "clave foránea" en MongoDB
        nuevoPaciente.setNombre(nombre);
        nuevoPaciente.setCitas(new ArrayList<>()); // Inicializamos la lista de citas vacía
        pacienteRepository.save(nuevoPaciente);
    }

    public Paciente obtenerPorUsuarioId(String email) {
        return pacienteRepository.findByUsuarioId(email)
                .orElseThrow(() -> new PerfilNoEncontradoException("No se encontró el perfil de paciente para el email: " + email));
    }

    public void actualizarDatosPersonales(String email, String nuevoNombre) {
        Paciente p = obtenerPorUsuarioId(email);
        p.setNombre(nuevoNombre);
        pacienteRepository.save(p);
    }
}
