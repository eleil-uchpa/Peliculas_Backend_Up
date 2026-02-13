package com.example.TareaFinal.repository;

import com.example.TareaFinal.entity.PeliculaEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PeliculaRepository extends JpaRepository <PeliculaEntity, Integer> {

    Optional<PeliculaEntity> findByNombre(String nombre);

    List<PeliculaEntity> findByNombreContainingIgnoreCase(String nombre);


    @Query(
            value = """
        SELECT DISTINCT p.*
        FROM pelicula p
        LEFT JOIN pelicula_categoria pc ON p.id_pelicula = pc.id_pelicula
        LEFT JOIN categoria c ON pc.id_categoria = c.id_categoria
        WHERE (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
          AND (:anioEstreno IS NULL OR p.anio_estreno = :anioEstreno)
          AND (:categoria IS NULL OR LOWER(c.name) = LOWER(:categoria))
        """,
            countQuery = """
        SELECT COUNT(DISTINCT p.id_pelicula)
        FROM pelicula p
        LEFT JOIN pelicula_categoria pc ON p.id_pelicula = pc.id_pelicula
        LEFT JOIN categoria c ON pc.id_categoria = c.id_categoria
        WHERE (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
          AND (:anioEstreno IS NULL OR p.anio_estreno = :anioEstreno)
          AND (:categoria IS NULL OR LOWER(c.name) = LOWER(:categoria))
        """,
            nativeQuery = true
    )
    Page<PeliculaEntity> buscarPeliculas(
            @Param("nombre") String nombre,
            @Param("anioEstreno") Integer anioEstreno,
            @Param("categoria") String categoria,
            Pageable pageable
    );
}
