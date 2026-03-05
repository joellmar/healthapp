package com.joselumartos.healthapp.dto;

import com.joselumartos.healthapp.model.Medico;
import com.joselumartos.healthapp.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicoMapper {
    MedicoMapper INSTANCE = Mappers.getMapper(MedicoMapper.class);

    MedicoDTO toDTO(Medico medico);

    List<MedicoDTO> toDTOList(List<Medico> medicos);

    Medico toDocument(MedicoDTO medicoDTO);
}
