package com.joselumartos.healthapp.repository;

import com.joselumartos.healthapp.model.Paciente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends MongoRepository<Paciente, String> {
    Optional<Paciente> findByUsuarioId(String usuarioId);
}
