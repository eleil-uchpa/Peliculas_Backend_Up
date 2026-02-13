package com.example.TareaFinal.dto.response;

import jakarta.persistence.Column;
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
public class PeliculaResponse {
    private int id;
    private String nombre;
    private Integer anioEstreno;
    private String synopsis;
    private Date createAt;
    private String imagenUrl;
    private String usuarioPelicula;
    private List<String> categorias;

}
