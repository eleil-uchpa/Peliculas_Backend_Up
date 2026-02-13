package com.example.TareaFinal.repository;

import com.example.TareaFinal.entity.CalificacionEntity;
import com.example.TareaFinal.entity.PeliculaEntity;
import com.example.TareaFinal.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CalificacionRepository extends JpaRepository<CalificacionEntity, Integer> {
    Optional<CalificacionEntity> findByUsuarioAndPelicula(UsuarioEntity usuario, PeliculaEntity pelicula);
    List<CalificacionEntity> findByUsuario(UsuarioEntity usuario);
}
