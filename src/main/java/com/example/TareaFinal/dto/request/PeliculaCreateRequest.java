package com.example.TareaFinal.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PeliculaCreateRequest {
    private String nombre;
    private Integer anioEstreno;
    private String synopsis;
    private String imagenUrl;
    private List<String> categorias;
}
