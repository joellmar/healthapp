package com.joselumartos.healthapp.dto;

import java.io.Serializable;
import java.time.LocalDate;

public record PacienteDTO(
        String nombre,
        String apellidos,
        LocalDate fechaNacimiento,
        String telefono,
        String usuarioId
) implements Serializable {

}
