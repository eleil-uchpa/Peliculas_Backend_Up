package com.example.TareaFinal.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioCreateRequest {
    private String correo;
    private String password;
    private String role;
}
