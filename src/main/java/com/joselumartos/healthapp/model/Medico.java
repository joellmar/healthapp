package com.joselumartos.healthapp.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "medicos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Medico {
    @Id
    private String id;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String especialidad;
    @Indexed(unique = true)
    private String usuarioId;
    private List<CitaMedica> citas;
}
