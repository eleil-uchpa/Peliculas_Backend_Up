package com.example.TareaFinal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pelicula_categoria",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_pelicula", "id_categoria"}))
public class CatePeliEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catePeli")
    private int peliCateId;

    // Relaciones
    @ManyToOne
    @JoinColumn(name = "id_pelicula", nullable = false)
    private PeliculaEntity pelicula;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaEntity categoria;
}
