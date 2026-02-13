package com.example.TareaFinal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalificacionResponse {
    private Integer idCalificacion;
    private Integer rating;
    private Integer idPeli;
    private String nombrePelicula;
    private String correoUsuario;
}
