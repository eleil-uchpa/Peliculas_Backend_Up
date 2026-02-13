package com.example.TareaFinal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "pelicula")
public class PeliculaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pelicula")
    private int peliculaId;
    private String nombre;
    @Column(name = "anio_estreno")
    private Integer anioEstreno;
    private String synopsis;
    @Column(name = "create_at")
    private Date createAt;
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuarioEntity;
}
