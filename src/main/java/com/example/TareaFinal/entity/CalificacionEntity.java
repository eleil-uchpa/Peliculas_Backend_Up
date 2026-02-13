package com.example.TareaFinal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "calificacion",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_usuario", "id_pelicula"}))

public class CalificacionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_calificacion")
    private int calificacionId;

    @Column(nullable = false)
    private Integer rating; // 1–5

    // Relaciones
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "id_pelicula", nullable = false)
    private PeliculaEntity pelicula;
}
