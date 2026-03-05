package com.joselumartos.healthapp.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "usuarios")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Usuario {
    @Id
    private String id;
    private String username;
    private String password;
    @Indexed(unique = true)
    private String email;
    private Set<String> roles;

}
