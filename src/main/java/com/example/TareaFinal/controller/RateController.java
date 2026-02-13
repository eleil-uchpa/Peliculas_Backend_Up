package com.example.TareaFinal.controller;

import com.example.TareaFinal.dto.request.RateRequest;
import com.example.TareaFinal.dto.response.CalificacionResponse;
import com.example.TareaFinal.dto.response.RateResponse;
import com.example.TareaFinal.dto.response.ResponseBase;
import com.example.TareaFinal.service.CalificacionService;
import com.example.TareaFinal.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rate")
@Tag(name = "Rate controller", description = "Controlador para la creacion, eliminacion y obtencion de ratings")
public class RateController {

    private final CalificacionService calificacionService;
    private final JwtService jwtService;

    public RateController(CalificacionService calificacionService, JwtService jwtService) {
        this.calificacionService = calificacionService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Registrar o actualizar una calificación de una película")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Calificación registrada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo éxito",
                                    value = """
                                            {
                                              "codigo": 200,
                                              "mensaje": "Calificación registrada correctamente",
                                              "data": {
                                                "nomPelicula": "El Padrino",
                                                "rating": 5
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Usuario no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "codigo": 400,
                                          "mensaje": "Usuario no encontrado",
                                          "data": null
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token no proporcionado o inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "codigo": 401,
                                          "mensaje": "Token no proporcionado o inválido",
                                          "data": null
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Película no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "codigo": 404,
                                          "mensaje": "Película no encontrada",
                                          "data": null
                                        }
                                        """
                            )
                    )
            )
    })
    @PostMapping("/save")
    public ResponseBase<RateResponse> rateMovie(
            @RequestBody RateRequest request) {
            return calificacionService.calificarPelicula(request);
    }

    @Operation(summary = "Listar todas las calificaciones realizadas por el usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Calificaciones obtenidas correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo éxito",
                                    value = """
                                        {
                                          "codigo": 200,
                                          "mensaje": "Calificaciones obtenidas correctamente",
                                          "data": [
                                            { "nomPelicula": "El Padrino", "rating": 5 },
                                            { "nomPelicula": "Inception", "rating": 4 }
                                          ]
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token no proporcionado o inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "codigo": 401,
                                          "mensaje": "Token no proporcionado o inválido",
                                          "data": null
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado o sin calificaciones",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "codigo": 404,
                                          "mensaje": "El usuario no tiene calificaciones registradas",
                                          "data": []
                                        }
                                        """
                            )
                    )
            )
    })

    @GetMapping("/get")
    public ResponseBase<List<RateResponse>> listarCalificacionesUsuario(
            @RequestHeader("Authorization") String authHeader) {

//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return new ResponseBase<>(401, "Token no proporcionado", null);
//        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        return calificacionService.listarCalificacionesUsuario(username);
    }

    @Operation(summary = "Eliminar una calificación de una película realizada por el usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Calificación eliminada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo éxito",
                                    value = """
                                        {
                                          "codigo": 200,
                                          "mensaje": "Calificación eliminada correctamente",
                                          "data": null
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "El usuario no ha calificado esta película",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "codigo": 400,
                                          "mensaje": "No se encontró una calificación registrada para esta película",
                                          "data": null
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token no proporcionado o inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "codigo": 401,
                                          "mensaje": "Token no proporcionado o inválido",
                                          "data": null
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario o película no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "codigo": 404,
                                          "mensaje": "Usuario o película no encontrada",
                                          "data": null
                                        }
                                        """
                            )
                    )
            )
    })
    @DeleteMapping("/delete/{idPeli}")
    public ResponseBase<String> eliminarRate(
            @PathVariable int idPeli,
            Authentication authentication) {

        return calificacionService.eliminarCalificacion(idPeli, authentication);
    }

    @GetMapping("/findAll")
    public List<CalificacionResponse> getAllCalificaciones() {
        return calificacionService.findAll();
    }

    @DeleteMapping("/deleteAdmin")
    public ResponseBase<String> eliminarRateAdmin(
            @RequestParam int idPelicula,
            @RequestParam String correo) {

        return calificacionService.eliminarCalificacionAdmin(idPelicula, correo);
    }

}
