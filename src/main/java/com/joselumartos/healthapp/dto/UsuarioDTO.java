package com.joselumartos.healthapp.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record UsuarioDTO(
        @NotBlank
        @Size(min = 3, max = 20)
        String username,

        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "Debe tener un formato de correo válido")
        String email,

        @NotBlank
        @Size(min = 8)
        String password,

        String rol // MEDICO o PACIENTE
) implements Serializable { }