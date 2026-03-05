package com.joselumartos.healthapp.model;

import lombok.*;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CitaMedica {
    private String id;
    private LocalDate fecha;
    private LocalTime hora;
    private String especialidad;
    private String descripcion;
    private EstadoCita estado;
    private String medicoEmail;
    private String pacienteEmail;
}
