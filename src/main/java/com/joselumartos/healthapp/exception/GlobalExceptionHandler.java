package com.joselumartos.healthapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Recurso no encontrado (Ej: Buscas una cita que no existe)
    @ExceptionHandler(CitaNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // Devuelve un 404
    public String handleNoEncontrado(CitaNoEncontradaException ex, Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("errorTitle", "Recurso no encontrado");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(PerfilNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // Devuelve un 404
    public String handleNoEncontrado(PerfilNoEncontradoException ex, Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("errorTitle", "Recurso no encontrado");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    // 2. Acceso prohibido (Ej: Cancelar la cita de otro)
    @ExceptionHandler(AccesoDenegadoException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // Devuelve un 403
    public String handleAccesoDenegado(AccesoDenegadoException ex, Model model) {
        model.addAttribute("status", 403);
        model.addAttribute("errorTitle", "Acceso Denegado");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    // 3. Regla de negocio violada (Ej: Cancelar cita ya realizada)
    @ExceptionHandler(EstadoCitaInvalidoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Devuelve un 400
    public String handleEstadoInvalido(EstadoCitaInvalidoException ex, Model model) {
        model.addAttribute("status", 400);
        model.addAttribute("errorTitle", "Operación no válida");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    // 4. Validaciones de formularios (Mantenemos el código pero lo adaptamos a la vista)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationErrors(MethodArgumentNotValidException ex, Model model) {
        StringBuilder errores = new StringBuilder("Revisa los campos: ");
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.append(error.getField()).append(" (").append(error.getDefaultMessage()).append("); ");
        });

        model.addAttribute("status", 400);
        model.addAttribute("errorTitle", "Error de Validación");
        model.addAttribute("errorMessage", errores.toString());
        return "error";
    }

    // 5. El comodín para todo lo demás (Fallos del servidor)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("status", 500);
        model.addAttribute("errorTitle", "Error interno del servidor");
        model.addAttribute("errorMessage", "Ups, algo se ha roto por dentro. Inténtalo de nuevo más tarde.");
        // Opcional en desarrollo: model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}