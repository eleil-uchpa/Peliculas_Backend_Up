package com.example.TareaFinal.service;

import com.example.TareaFinal.dto.request.LoginRequest;
import com.example.TareaFinal.dto.request.UsuarioCreateRequest;
import com.example.TareaFinal.dto.response.ResponseBase;
import com.example.TareaFinal.dto.response.UsuarioResponse;

import java.util.List;

public interface UsuarioService {
    UsuarioResponse createUser(UsuarioCreateRequest usuarioCreateRequest);
    String login(LoginRequest loginRequest);
    List<UsuarioResponse> findAll();
    ResponseBase <String> eliminarUsuario (int idUsuario);
}
