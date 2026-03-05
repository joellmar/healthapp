package com.joselumartos.healthapp.dto;

import com.joselumartos.healthapp.model.Paciente;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PacienteMapper {
    PacienteMapper INSTANCE = Mappers.getMapper(PacienteMapper.class);

    PacienteDTO toDTO(Paciente paciente);

    List<PacienteDTO> toDTOList(List<Paciente> pacientes);

    Paciente toDocument(PacienteDTO pacienteDTO);
}
