package com.example.TareaFinal.service;

import com.example.TareaFinal.dto.response.CategoriaResponse;
import com.example.TareaFinal.dto.response.PeliculaResponse;

import java.util.List;

public interface CategoriaService {
    List<CategoriaResponse> findAll();
}
