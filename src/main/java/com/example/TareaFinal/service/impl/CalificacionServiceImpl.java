package com.example.TareaFinal.service.impl;

import com.example.TareaFinal.dto.request.RateRequest;
import com.example.TareaFinal.dto.response.CalificacionResponse;
import com.example.TareaFinal.dto.response.RateResponse;
import com.example.TareaFinal.dto.response.ResponseBase;
import com.example.TareaFinal.entity.CalificacionEntity;
import com.example.TareaFinal.entity.PeliculaEntity;
import com.example.TareaFinal.entity.UsuarioEntity;
import com.example.TareaFinal.repository.CalificacionRepository;
import com.example.TareaFinal.repository.PeliculaRepository;
import com.example.TareaFinal.repository.UsuarioRepository;
import com.example.TareaFinal.service.CalificacionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CalificacionServiceImpl implements CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final PeliculaRepository peliculaRepository;
    private final UsuarioRepository usuarioRepository;

    public CalificacionServiceImpl(CalificacionRepository calificacionRepository, PeliculaRepository peliculaRepository, UsuarioRepository usuarioRepository) {
        this.calificacionRepository = calificacionRepository;
        this.peliculaRepository = peliculaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public ResponseBase<RateResponse> calificarPelicula(RateRequest request) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByCorreo(username);
        if (usuarioOpt.isEmpty()) {
            return new ResponseBase<>(400, "Usuario no encontrado", null);
        }

        Optional<PeliculaEntity> peliculaOpt = peliculaRepository.findById(request.getIdPelicula());

        if (peliculaOpt.isEmpty()) {
            return new ResponseBase<>(404, "Película no encontrada", null);
        }

        UsuarioEntity usuario = usuarioOpt.get();
        PeliculaEntity pelicula = peliculaOpt.get();

        Optional<CalificacionEntity> existente = calificacionRepository.findByUsuarioAndPelicula(usuario, pelicula);

        CalificacionEntity calificacion;

        if (existente.isPresent()) {
            calificacion = existente.get();
            calificacion.setRating(request.getRate());
        } else {
            calificacion = new CalificacionEntity();
            calificacion.setUsuario(usuario);
            calificacion.setPelicula(pelicula);
            calificacion.setRating(request.getRate());
        }

        calificacionRepository.save(calificacion);

        RateResponse response = new RateResponse(
                calificacion.getRating(),
                pelicula.getNombre()
        );

        return new ResponseBase<>(200, "Calificación registrada correctamente", response);
    }

    @Override
    public ResponseBase<List<RateResponse>> listarCalificacionesUsuario(String username) {
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByCorreo(username);
        if (usuarioOpt.isEmpty()) {
            return new ResponseBase<>(400, "Usuario no encontrado", null);
        }

        UsuarioEntity usuario = usuarioOpt.get();
        List<CalificacionEntity> calificaciones = calificacionRepository.findByUsuario(usuario);

        if (calificaciones.isEmpty()) {
            return new ResponseBase<>(200, "El usuario no tiene calificaciones registradas", null);
        }

//        List<RateResponse> responseList = calificaciones.stream()
//                .map(c -> new RateResponse(c.getRating(), c.getPelicula().getNombre()))
//                .toList();

        List<RateResponse> responseList = new ArrayList<>();
        for (CalificacionEntity c : calificaciones) {
            Integer rating = c.getRating();
            String nombrePelicula = c.getPelicula().getNombre();
            RateResponse rateResponse = new RateResponse(rating, nombrePelicula);
            responseList.add(rateResponse);
        }

        return new ResponseBase<>(200, "Calificaciones obtenidas correctamente", responseList);
    }

    @Override
    public ResponseBase<String> eliminarCalificacion(int idPeli, Authentication auth) {
        String email = auth.getName();

        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByCorreo(email);
        if (usuarioOpt.isEmpty()) {
            return new ResponseBase<>(400, "Usuario no encontrado", null);
        }

        Optional<PeliculaEntity> peliculaOpt = peliculaRepository.findById(idPeli);

        if (peliculaOpt.isEmpty()) {
            return new ResponseBase<>(404, "Película no encontrada", null);
        }

        UsuarioEntity usuario = usuarioOpt.get();
        PeliculaEntity pelicula = peliculaOpt.get();

        Optional<CalificacionEntity> existente = calificacionRepository.findByUsuarioAndPelicula(usuario, pelicula);

        if (existente.isEmpty()) {
            return new ResponseBase<>(404, "No existe calificación para eliminar", null);
        }

        calificacionRepository.delete(existente.get());

        return new ResponseBase<>(200, "Calificación eliminada correctamente", null);

    }

    @Override
    public ResponseBase<String> eliminarCalificacionAdmin(int idPelicula, String correo) {
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            return new ResponseBase<>(400, "Usuario no encontrado", null);
        }
        Optional<PeliculaEntity> peliculaOpt = peliculaRepository.findById(idPelicula);
        if (peliculaOpt.isEmpty()){
            return new ResponseBase<>(404, "Película no encontrada", null);
        }

        UsuarioEntity usuario = usuarioOpt.get();
        PeliculaEntity pelicula = peliculaOpt.get();

        Optional<CalificacionEntity> existente = calificacionRepository.findByUsuarioAndPelicula(usuario, pelicula);
        if (existente.isEmpty()) {
            return new ResponseBase<>(404, "No existe calificación para eliminar", null);
        }

        calificacionRepository.delete(existente.get());

        return new ResponseBase<>(200, "Calificación eliminada correctamente", null);

    }

    @Override
    public List<CalificacionResponse> findAll() {
        List<CalificacionEntity> lista = calificacionRepository.findAll();

        return lista.stream()
                .map(calificacion -> new CalificacionResponse(
                        calificacion.getCalificacionId(),
                        calificacion.getRating(),
                        calificacion.getPelicula().getPeliculaId(),
                        calificacion.getPelicula().getNombre(),
                        calificacion.getUsuario().getCorreo()
                ))
                .toList();
    }
}
