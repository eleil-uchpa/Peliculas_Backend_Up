package com.example.TareaFinal.service;

import com.example.TareaFinal.dto.request.RateRequest;
import com.example.TareaFinal.dto.response.CalificacionResponse;
import com.example.TareaFinal.dto.response.RateResponse;
import com.example.TareaFinal.dto.response.ResponseBase;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CalificacionService {
    ResponseBase<RateResponse> calificarPelicula(RateRequest request);
    ResponseBase<List<RateResponse>> listarCalificacionesUsuario(String username);
    ResponseBase<String> eliminarCalificacion(int idPeli, Authentication auth);
    ResponseBase<String> eliminarCalificacionAdmin( int idPelicula, String correo);
    List<CalificacionResponse> findAll();
}
