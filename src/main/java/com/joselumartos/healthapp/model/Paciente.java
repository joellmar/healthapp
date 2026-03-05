package com.joselumartos.healthapp.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "pacientes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Paciente {
    @Id
    private String id;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String telefono;
    @Indexed(unique = true)
    private String usuarioId;
    private List<CitaMedica> citas;
}
