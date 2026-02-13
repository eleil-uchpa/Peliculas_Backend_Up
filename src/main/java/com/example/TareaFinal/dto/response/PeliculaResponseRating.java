package com.example.TareaFinal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PeliculaResponseRating {
    private int id;
    private String nombre;
    private Integer anioEstreno;
    private String synopsis;
    private String imagenUrl;
    private List<String> categorias;
    private Integer miRating;
}
