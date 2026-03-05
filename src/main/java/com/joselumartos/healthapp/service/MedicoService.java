package com.joselumartos.healthapp.service;

import com.joselumartos.healthapp.exception.PerfilNoEncontradoException;
import com.joselumartos.healthapp.model.Medico;
import com.joselumartos.healthapp.repository.MedicoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicoService {
    private final MedicoRepository medicoRepository;

    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    public void crearPerfilMedico(String email, String nombre, String especialidad) {
        Medico m = new Medico();
        m.setUsuarioId(email);
        m.setNombre(nombre);
        m.setEspecialidad(especialidad);
        m.setCitas(new ArrayList<>());
        medicoRepository.save(m);
    }

    public Medico obtenerPorUsuarioId(String email) {
        return medicoRepository
                .findByUsuarioId(email)
                .orElseThrow(() -> new PerfilNoEncontradoException("No se encontró el perfil de médico para el email: " + email));
    }

    public List<Medico> obtenerTodos() {
        return medicoRepository.findAll();
    }
}
