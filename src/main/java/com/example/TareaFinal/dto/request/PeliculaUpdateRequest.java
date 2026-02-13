package com.example.TareaFinal.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class PeliculaUpdateRequest {
    private String nombre;
    private Integer anioEstreno;
    private String synopsis;
    private String imagenUrl;
    private List<String> categorias;
}
