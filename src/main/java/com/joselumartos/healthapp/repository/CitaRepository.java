package com.joselumartos.healthapp.repository;

import com.joselumartos.healthapp.model.CitaMedica;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository extends MongoRepository<CitaMedica, String> {
}
