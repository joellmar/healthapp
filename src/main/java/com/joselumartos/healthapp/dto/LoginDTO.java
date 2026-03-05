package com.joselumartos.healthapp.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;


public record LoginDTO(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Debe tener un formato de correo válido (ej: usuario@dominio.com)")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) implements Serializable {}
