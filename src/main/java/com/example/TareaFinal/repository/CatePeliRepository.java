package com.example.TareaFinal.repository;

import com.example.TareaFinal.entity.CatePeliEntity;
import com.example.TareaFinal.entity.PeliculaEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatePeliRepository extends JpaRepository<CatePeliEntity, Integer> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM pelicula_categoria WHERE id_pelicula = :peliculaId", nativeQuery = true)
    int deleteByPeliculaId(@Param("peliculaId") int peliculaId);

    List<CatePeliEntity> findByPelicula(PeliculaEntity pelicula);
}
