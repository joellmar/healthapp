package com.joselumartos.healthapp.service;

import com.joselumartos.healthapp.dto.CitaDTO;
import com.joselumartos.healthapp.dto.CitaMapper;
import com.joselumartos.healthapp.exception.AccesoDenegadoException;
import com.joselumartos.healthapp.exception.CitaNoEncontradaException;
import com.joselumartos.healthapp.exception.EstadoCitaInvalidoException;
import com.joselumartos.healthapp.model.CitaMedica;
import com.joselumartos.healthapp.model.EstadoCita;
import com.joselumartos.healthapp.model.Medico;
import com.joselumartos.healthapp.model.Paciente;
import com.joselumartos.healthapp.repository.MedicoRepository;
import com.joselumartos.healthapp.repository.PacienteRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CitaService {
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;
    private final CitaMapper citaMapper;

    public CitaService(PacienteRepository pacienteRepository, MedicoRepository medicoRepository, PacienteService pacienteService, MedicoService medicoService, CitaMapper citaMapper) {
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
        this.citaMapper = citaMapper;
    }

    @Transactional
    public void crearCita(String emailPaciente, CitaDTO citaDTO) {
        Paciente paciente = pacienteService.obtenerPorUsuarioId(emailPaciente);
        Medico medico = medicoService.obtenerPorUsuarioId(citaDTO.medicoEmail());

        CitaMedica cita = citaMapper.toDocument(citaDTO);
        cita.setId(UUID.randomUUID().toString());
        cita.setEstado(EstadoCita.PROGRAMADA);

        // Guardamos las referencias cruzadas
        cita.setPacienteEmail(emailPaciente);
        cita.setMedicoEmail(medico.getUsuarioId());

        paciente.getCitas().add(cita);
        medico.getCitas().add(cita);

        pacienteRepository.save(paciente);
        medicoRepository.save(medico);
    }

    @Transactional
    public void cancelarCita(String citaId, String emailUsuario, String rol) {
        if (rol.equals("ROLE_PACIENTE")) {
            Paciente p = pacienteService.obtenerPorUsuarioId(emailUsuario);
            CitaMedica cita = getCitaFromPaciente(citaId, p);

            if (!cita.getPacienteEmail().equals(emailUsuario)) {
                throw new AccesoDenegadoException("Acceso denegado: El email del usuario logueado no coincide con el de la cita.");            }

            if (cita.getEstado() == EstadoCita.REALIZADA) {
                throw new EstadoCitaInvalidoException("Operación no permitida: No se puede cancelar una cita que ya ha sido realizada.");            }

            cita.setEstado(EstadoCita.CANCELADA);
            pacienteRepository.save(p);
            cancelarCitaMedico(citaId, cita);

        } else if (rol.equals("ROLE_MEDICO")) {
            Medico m = medicoService.obtenerPorUsuarioId(emailUsuario);
            CitaMedica cita = getCitaFromMedico(citaId, m);

            if (!cita.getMedicoEmail().equals(emailUsuario)) {
                throw new AccesoDenegadoException("Acceso denegado: El email del usuario logueado no coincide con el de la cita.");            }

            if (cita.getEstado() == EstadoCita.REALIZADA) {
                throw new EstadoCitaInvalidoException("Operación no permitida: No se puede cancelar una cita que ya ha sido realizada.");            }

            cita.setEstado(EstadoCita.CANCELADA);
            medicoRepository.save(m);
            cancelarCitaPaciente(citaId, cita);
        }

    }

    private @NonNull CitaMedica getCitaFromPaciente(String citaId, Paciente paciente) {
        return paciente.getCitas()
                .stream()
                .filter(c -> c.getId().equals(citaId))
                .findFirst()
                .orElseThrow(() -> new CitaNoEncontradaException("No se ha encontrado ninguna cita con el ID proporcionado."));
    }

    private @NonNull CitaMedica getCitaFromMedico(String citaId, Medico medico) {
        return medico.getCitas()
                .stream()
                .filter(c -> c.getId().equals(citaId))
                .findFirst()
                .orElseThrow(() -> new CitaNoEncontradaException("No se ha encontrado ninguna cita con el ID proporcionado."));
    }

    private void cancelarCitaMedico(String citaId, CitaMedica cita) {
        Medico m = medicoService.obtenerPorUsuarioId(cita.getMedicoEmail());
        m.getCitas()
                .stream()
                .filter(c -> c.getId().equals(citaId))
                .forEach(c -> c.setEstado(EstadoCita.CANCELADA));
        medicoRepository.save(m);
    }

    private void cancelarCitaPaciente(String citaId, CitaMedica cita) {
        Paciente p = pacienteService.obtenerPorUsuarioId(cita.getPacienteEmail());
        p.getCitas()
                .stream()
                .filter(c -> c.getId().equals(citaId))
                .forEach(c -> c.setEstado(EstadoCita.CANCELADA));
        pacienteRepository.save(p);
    }

    @Transactional
    public void editarCitaSegura(String id, CitaDTO dtoActualizado, String emailPaciente) {
        // 1. Buscamos y validamos la cita original en el paciente
        Paciente p = pacienteService.obtenerPorUsuarioId(emailPaciente);
        CitaMedica citaPaciente = p.getCitas().stream()
                .filter(c -> c.getId().equals(id)).findFirst()
                .orElseThrow(() -> new CitaNoEncontradaException("Cita no encontrada"));

        if(citaPaciente.getEstado() == EstadoCita.CANCELADA || citaPaciente.getEstado() == EstadoCita.REALIZADA){
            throw new EstadoCitaInvalidoException("Solo puedes editar citas PROGRAMADAS.");
        }

        String emailMedicoOriginal = citaPaciente.getMedicoEmail();

        // Dentro de editarCitaSegura, al actualizar la cita del paciente:
        citaPaciente.setFecha(dtoActualizado.fecha());
        citaPaciente.setHora(dtoActualizado.hora());
        citaPaciente.setDescripcion(dtoActualizado.descripcion());

        // Si ha cambiado de médico, hay que hacer "cirugía" mayor
        if (!emailMedicoOriginal.equals(dtoActualizado.medicoEmail())) {
            // Borrar del médico viejo
            Medico medicoViejo = medicoService.obtenerPorUsuarioId(emailMedicoOriginal);
            medicoViejo.getCitas().removeIf(c -> c.getId().equals(id));
            medicoRepository.save(medicoViejo);

            // Añadir al médico nuevo
            Medico medicoNuevo = medicoService.obtenerPorUsuarioId(dtoActualizado.medicoEmail());
            citaPaciente.setMedicoEmail(medicoNuevo.getUsuarioId());
            medicoNuevo.getCitas().add(citaPaciente);
            medicoRepository.save(medicoNuevo);
        } else {
            // Mismo médico, solo actualizamos los datos de esa cita en su lista
            Medico medico = medicoService.obtenerPorUsuarioId(emailMedicoOriginal);
            CitaMedica citaMedico = medico.getCitas().stream().filter(c -> c.getId().equals(id)).findFirst().get();
            citaMedico.setFecha(dtoActualizado.fecha());
            citaMedico.setHora(dtoActualizado.hora());
            citaMedico.setDescripcion(dtoActualizado.descripcion());
            medicoRepository.save(medico);


        }

        pacienteRepository.save(p);
    }
}
