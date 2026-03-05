package com.joselumartos.healthapp.dto;

import com.joselumartos.healthapp.model.CitaMedica;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CitaMapper {
    CitaMapper INSTANCE = Mappers.getMapper(CitaMapper.class);

    CitaDTO toDTO(CitaMedica cita);

    List<CitaDTO> toDTOList(List<CitaMedica> citas);

    CitaMedica toDocument(CitaDTO citaDTO);
}