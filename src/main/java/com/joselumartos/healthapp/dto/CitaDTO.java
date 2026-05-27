package com.joselumartos.healthapp.dto;

import com.joselumartos.healthapp.model.EstadoCita;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public record CitaDTO(
        @NotNull(message = "Debes indicar una fecha")
        @FutureOrPresent(message = "La fecha no puede ser en el pasado")
        LocalDate fecha,

        @NotNull(message = "Debes indicar una hora")
        @DateTimeFormat(pattern = "HH:mm")
        LocalTime hora,

        String especialidad,

        @NotBlank(message = "El motivo de la consulta es obligatorio")
        @Size(min = 5, message = "El motivo debe ser más descriptivo (mín. 5 caracteres)")
        String descripcion,

        EstadoCita estado,

        @NotBlank(message = "Debes seleccionar un médico")
        String medicoEmail
) implements Serializable {
}