package com.joselumartos.healthapp.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record RegistroDTO(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Debe tener un formato de correo válido (ej: usuario@dominio.com)")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).*$", message = "Debe contener al menos una mayúscula y un número")
        String password,

        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(min = 3, message = "El nombre debe tener al menos 3 letras")
        String nombre,

        String especialidad, // Solo si es médico

        @NotBlank(message = "Debes seleccionar un rol")
        String rol // "ROLE_PACIENTE" o "ROLE_MEDICO"
) implements Serializable {
}