package com.joselumartos.healthapp.service;

import com.joselumartos.healthapp.dto.RegistroDTO;
import com.joselumartos.healthapp.model.Usuario;
import com.joselumartos.healthapp.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          PacienteService pacienteService, MedicoService medicoService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
    }

    /**
     * Crear un usuario nuevo (registro)
     */
    @Transactional // 🛡️ O todo o nada
    public void registrarNuevoUsuario(RegistroDTO dto) {
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        // 1. Creamos el Usuario (Seguridad)
        Usuario user = new Usuario();
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Set.of(dto.rol()));
        usuarioRepository.save(user);

        // 2. Creamos el Perfil según el rol
        if ("ROLE_MEDICO".equals(dto.rol())) {
            medicoService.crearPerfilMedico(dto.email(), dto.nombre(), dto.especialidad());
        } else {
            pacienteService.crearPerfilPaciente(dto.email(), dto.nombre());
        }
    }

    /**
     * Buscar usuario por username (útil fuera de Security)
     */
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    /**
     * Buscar por id
     */
    public Optional<Usuario> buscarPorId(String id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Eliminar usuario (solo admin)
     */
    public void eliminarUsuario(String id) {
        usuarioRepository.deleteById(id);
    }
}
