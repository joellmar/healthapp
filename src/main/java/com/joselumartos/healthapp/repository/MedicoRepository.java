package com.joselumartos.healthapp.repository;

import com.joselumartos.healthapp.model.Medico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicoRepository extends MongoRepository<Medico, String> {
    Optional<Medico> findByUsuarioId(String usuarioId);
}
