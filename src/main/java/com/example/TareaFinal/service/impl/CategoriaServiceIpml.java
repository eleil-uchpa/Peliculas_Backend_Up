package com.example.TareaFinal.service.impl;


import com.example.TareaFinal.dto.response.CategoriaResponse;
import com.example.TareaFinal.repository.CategoriaRepository;
import com.example.TareaFinal.service.CategoriaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceIpml implements CategoriaService {
    private CategoriaRepository categoriaRepository;

    public CategoriaServiceIpml(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<CategoriaResponse> findAll() {
        return categoriaRepository.findAll()
                .stream()
                .map(categoria -> new CategoriaResponse(
                        categoria.getCategoriaId(),
                        categoria.getName()
                ))
                .toList();
    }
}
