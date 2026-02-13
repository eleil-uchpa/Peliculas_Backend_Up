package com.example.TareaFinal.service.impl;

import com.example.TareaFinal.dto.request.PeliculaCreateRequest;
import com.example.TareaFinal.dto.request.PeliculaUpdateRequest;
import com.example.TareaFinal.dto.response.PeliculaResponse;
import com.example.TareaFinal.dto.response.PeliculaResponseRating;
import com.example.TareaFinal.dto.response.ResponseBase;
import com.example.TareaFinal.entity.*;
import com.example.TareaFinal.repository.*;
import com.example.TareaFinal.service.PeliculaService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class PeliculaServiceImpl implements PeliculaService {

    private final PeliculaRepository peliculaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final CatePeliRepository catePeliRepository;
    private final CalificacionRepository calificacionRepository;

    public PeliculaServiceImpl(PeliculaRepository peliculaRepository, UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository, CatePeliRepository catePeliRepository, CalificacionRepository calificacionRepository) {
        this.peliculaRepository = peliculaRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.catePeliRepository = catePeliRepository;
        this.calificacionRepository = calificacionRepository;
    }

    @Override
    public ResponseBase <PeliculaResponse>  crearPelicula(PeliculaCreateRequest peliculaCreateRequest, String correo) {

            Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByCorreo(correo);

            if (usuarioOpt.isEmpty()) {
                return new ResponseBase<>(403, "Usuario no encontrado", null);
            }
            UsuarioEntity usuario = usuarioOpt.get();

            List<String> categoriasRecibidas = peliculaCreateRequest.getCategorias();
            if (categoriasRecibidas == null || categoriasRecibidas.isEmpty()) {
                return new ResponseBase<>(400, "Debe enviar al menos una categoría", null);
            }

            List<CategoriaEntity> categoriasEncontradas = new ArrayList<>();
            for (String nombre : categoriasRecibidas) {
                Optional<CategoriaEntity> catOpt = categoriaRepository.findByName(nombre);
                if (catOpt.isEmpty()) {
                    return new ResponseBase<>(400, "La categoría '" + nombre + "' no existe", null);
                }
                categoriasEncontradas.add(catOpt.get());
            }

            PeliculaEntity pelicula = new PeliculaEntity();
            pelicula.setNombre(peliculaCreateRequest.getNombre());
            pelicula.setAnioEstreno(peliculaCreateRequest.getAnioEstreno());
            pelicula.setSynopsis(peliculaCreateRequest.getSynopsis());
            pelicula.setCreateAt(new Date());
            pelicula.setImagenUrl(peliculaCreateRequest.getImagenUrl());
            pelicula.setUsuarioEntity(usuario);
            peliculaRepository.save(pelicula);

            List<String> categoriasNombres = new ArrayList<>();
            for (CategoriaEntity categoria : categoriasEncontradas) {
                CatePeliEntity enlace = new CatePeliEntity();
                enlace.setPelicula(pelicula);
                enlace.setCategoria(categoria);
                catePeliRepository.save(enlace);

                categoriasNombres.add(categoria.getName());
            }

            PeliculaResponse response = new PeliculaResponse(
                    pelicula.getPeliculaId(),
                    pelicula.getNombre(),
                    pelicula.getAnioEstreno(),
                    pelicula.getSynopsis(),
                    pelicula.getCreateAt(),
                    pelicula.getImagenUrl(),
                    usuario.getCorreo(),
                    categoriasNombres
            );

            return new ResponseBase<>(200, "Película creada con éxito", response);
    }

    @Override
    public ResponseBase<PeliculaResponse> actualizarPelicula(int idPelicula, PeliculaUpdateRequest peliculaUpdateRequest) {

        Optional<PeliculaEntity> peliculaOpt = peliculaRepository.findById(idPelicula);
        if (peliculaOpt.isEmpty()) {
            return new ResponseBase<>(404, "Pelicula no encontrada", null);
        }
        PeliculaEntity pelicula = peliculaOpt.get();

        List<CategoriaEntity> categoriasEncontradas = new ArrayList<>();
        if (peliculaUpdateRequest.getCategorias() != null && !peliculaUpdateRequest.getCategorias().isEmpty()) {
            for (String nombreCat : peliculaUpdateRequest.getCategorias()) {
                Optional<CategoriaEntity> catOpt = categoriaRepository.findByName(nombreCat);
                if (catOpt.isEmpty()) {
                    return new ResponseBase<>(400, "La categoría '" + nombreCat + "' no existe", null);
                }
                categoriasEncontradas.add(catOpt.get());
            }
        }

        if (peliculaUpdateRequest.getNombre() != null) pelicula.setNombre(peliculaUpdateRequest.getNombre());
        if (peliculaUpdateRequest.getAnioEstreno() != null) pelicula.setAnioEstreno(peliculaUpdateRequest.getAnioEstreno());
        if (peliculaUpdateRequest.getSynopsis() != null) pelicula.setSynopsis(peliculaUpdateRequest.getSynopsis());
        if (peliculaUpdateRequest.getImagenUrl() != null) pelicula.setImagenUrl(peliculaUpdateRequest.getImagenUrl());

        if (!categoriasEncontradas.isEmpty()) {
            catePeliRepository.deleteByPeliculaId(pelicula.getPeliculaId());
            for (CategoriaEntity cat : categoriasEncontradas) {
                CatePeliEntity enlace = new CatePeliEntity();
                enlace.setPelicula(pelicula);
                enlace.setCategoria(cat);
                catePeliRepository.save(enlace);
            }
        }

        peliculaRepository.save(pelicula);


        List<String> categoriasFinales;
        if (!categoriasEncontradas.isEmpty()) {
            categoriasFinales = categoriasEncontradas.stream().map(CategoriaEntity::getName).toList();
        } else {
            categoriasFinales = catePeliRepository.findByPelicula(pelicula).stream()
                    .map(cp -> cp.getCategoria().getName()).toList();
        }

        PeliculaResponse response = new PeliculaResponse(
                pelicula.getPeliculaId(),
                pelicula.getNombre(),
                pelicula.getAnioEstreno(),
                pelicula.getSynopsis(),
                pelicula.getCreateAt(),
                pelicula.getImagenUrl(),
                pelicula.getUsuarioEntity().getCorreo(),
                categoriasFinales
        );

        return new ResponseBase<>(200, "Película actualizada correctamente", response);
    }

    @Override
    public ResponseBase<String> eliminarPelicula(int idPelicula) {

        Optional<PeliculaEntity> peliculaOpt = peliculaRepository.findById(idPelicula);
        if (peliculaOpt.isEmpty()) {
            return new ResponseBase<>(404, "Pelicula no encontrada", null);
        }
        PeliculaEntity pelicula = peliculaOpt.get();

        catePeliRepository.deleteByPeliculaId(pelicula.getPeliculaId());
        peliculaRepository.delete(pelicula);
        return new ResponseBase<>(200, "Película eliminada correctamente", null);

    }

    @Override
    public List<PeliculaResponse> findPeliculas(Pageable pageable, String nombre, Integer anioEstreno, String categoria) {
        if (nombre != null && nombre.trim().isEmpty()) nombre = null;
        if (categoria != null && categoria.trim().isEmpty()) categoria = null;

        Page<PeliculaEntity> peliculaEntities =
                peliculaRepository.buscarPeliculas(nombre, anioEstreno, categoria, pageable);

        List<PeliculaResponse> peliculaResponses = new ArrayList<>();

        for (PeliculaEntity entity : peliculaEntities) {
            PeliculaResponse peliculaResponse = new PeliculaResponse();
            peliculaResponse.setId(entity.getPeliculaId());
            peliculaResponse.setNombre(entity.getNombre());
            peliculaResponse.setAnioEstreno(entity.getAnioEstreno());
            peliculaResponse.setSynopsis(entity.getSynopsis());
            peliculaResponse.setCreateAt(entity.getCreateAt());
            peliculaResponse.setImagenUrl(entity.getImagenUrl());
            peliculaResponse.setUsuarioPelicula(entity.getUsuarioEntity().getCorreo());

            // Obtener las categorías de cada película
            List<CatePeliEntity> catePelis = catePeliRepository.findByPelicula(entity);
            List<String> nombresCategorias = catePelis.stream()
                    .map(cp -> cp.getCategoria().getName())
                    .collect(Collectors.toList());

            peliculaResponse.setCategorias(nombresCategorias);

            peliculaResponses.add(peliculaResponse);
        }

        return peliculaResponses;
    }

    @Override
    public List<PeliculaResponse> findAll() {
        List<PeliculaResponse> responses = new ArrayList<>();

        List<PeliculaEntity> lista = peliculaRepository.findAll();

        for (PeliculaEntity entity : lista) {

            PeliculaResponse response = new PeliculaResponse();
            response.setId(entity.getPeliculaId());
            response.setNombre(entity.getNombre());
            response.setAnioEstreno(entity.getAnioEstreno());
            response.setSynopsis(entity.getSynopsis());
            response.setCreateAt(entity.getCreateAt());
            response.setUsuarioPelicula(entity.getUsuarioEntity().getCorreo());

            // Obtener categorías
            List<CatePeliEntity> catePelis = catePeliRepository.findByPelicula(entity);
            List<String> categorias = catePelis.stream()
                    .map(cp -> cp.getCategoria().getName())
                    .toList();

            response.setCategorias(categorias);

            responses.add(response);
        }

        return responses;

    }

    @Override
    public List<PeliculaResponse> buscarPorId(int id) {

        Optional<PeliculaEntity> peliculaOpt = peliculaRepository.findById(id);
        PeliculaEntity entity = peliculaOpt.get();

        PeliculaResponse response = new PeliculaResponse();
        response.setId(entity.getPeliculaId());
        response.setNombre(entity.getNombre());
        response.setAnioEstreno(entity.getAnioEstreno());
        response.setSynopsis(entity.getSynopsis());
        response.setImagenUrl(entity.getImagenUrl());
        response.setCreateAt(entity.getCreateAt());
        response.setUsuarioPelicula(entity.getUsuarioEntity().getCorreo());

        List<CatePeliEntity> catePelis = catePeliRepository.findByPelicula(entity);
        List<String> categorias = catePelis.stream()
                .map(cp -> cp.getCategoria().getName())
                .toList();

        response.setCategorias(categorias);
        return List.of(response);

    }

    @Override
    public List<PeliculaResponseRating> findAllRate(Authentication auth) {

        String email = auth.getName();

        UsuarioEntity usuario = usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<PeliculaEntity> peliculas = peliculaRepository.findAll();

        List<PeliculaResponseRating> responseList = new ArrayList<>();

        for (PeliculaEntity pelicula : peliculas) {

            Optional<CalificacionEntity> calificacion = calificacionRepository.findByUsuarioAndPelicula(usuario, pelicula);

            Integer miRating = calificacion
                    .map(CalificacionEntity::getRating)
                    .orElse(null);

            List<CatePeliEntity> catePelis = catePeliRepository.findByPelicula(pelicula);
            List<String> categorias = catePelis.stream()
                    .map(cp -> cp.getCategoria().getName())
                    .toList();

            PeliculaResponseRating response = new PeliculaResponseRating(
                    pelicula.getPeliculaId(),
                    pelicula.getNombre(),
                    pelicula.getAnioEstreno(),
                    pelicula.getSynopsis(),
                    pelicula.getImagenUrl(),
                    categorias,
                    miRating
            );

            responseList.add(response);
        }

        return responseList;

    }

}
