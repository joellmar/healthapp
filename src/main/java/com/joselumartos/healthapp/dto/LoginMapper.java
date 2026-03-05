package com.joselumartos.healthapp.dto;

import com.joselumartos.healthapp.model.CitaMedica;
import com.joselumartos.healthapp.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoginMapper {
    LoginMapper INSTANCE = Mappers.getMapper(LoginMapper.class);

    LoginDTO toDTO(Usuario usuario);

    List<LoginDTO> toDTOList(List<Usuario> usuarios);

    Usuario toDocument(LoginDTO loginDTO);
}
