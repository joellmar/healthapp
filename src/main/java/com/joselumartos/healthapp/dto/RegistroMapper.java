package com.joselumartos.healthapp.dto;

import com.joselumartos.healthapp.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RegistroMapper {
    RegistroMapper INSTANCE = Mappers.getMapper(RegistroMapper.class);

    RegistroDTO toDTO(Usuario usuario);

    List<RegistroDTO> toDTOList(List<Usuario> usuarios);

    Usuario toDocument(RegistroDTO registroDTO);
}
