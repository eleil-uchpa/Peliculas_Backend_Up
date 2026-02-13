package com.example.TareaFinal.controller;

import com.example.TareaFinal.dto.request.PeliculaCreateRequest;
import com.example.TareaFinal.dto.request.PeliculaUpdateRequest;
import com.example.TareaFinal.dto.response.PeliculaResponse;
import com.example.TareaFinal.dto.response.PeliculaResponseRating;
import com.example.TareaFinal.dto.response.ResponseBase;
import com.example.TareaFinal.service.JwtService;
import com.example.TareaFinal.service.PeliculaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pelicula")
@Tag(name = "Pelicula controller", description = "Controlador para la creacion, actualizacion, eliminacion y obtencion de peliculas")
public class PeliculaController {
    private final PeliculaService peliculaService;
    private final JwtService jwtService;

    public PeliculaController(PeliculaService peliculaService, JwtService jwtService) {
        this.peliculaService = peliculaService;
        this.jwtService = jwtService;
    }

    @Operation(
            summary = "Endpoint para crear una película",
            description = "Permite registrar una nueva película en el sistema. Requiere autenticación mediante token JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Película creada con éxito",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo Creación Película",
                                    value = """
                                    {
                                      "codigo": 200,
                                      "mensaje": "Película creada con éxito",
                                      "data": {
                                          "id": 14,
                                          "nombre": "La La Land",
                                          "anioEstreno": 2016,
                                          "synopsis": "Un músico de jazz y una actriz se enamoran mientras persiguen sus sueños en Los Ángeles",
                                          "createAt": "2025-10-08T03:32:21.138+00:00",
                                          "usuarioPelicula": "eleil@gmail.com",
                                          "categorias": [
                                              "Romance",
                                              "Drama"
                                          ]
                                      }
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
                                    name = "Error Token",
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
                    responseCode = "403",
                    description = "Usuario no autenticado o sin permisos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(
                                    name = "Error Autenticación",
                                    value = """
                                    {
                                      "codigo": 403,
                                      "mensaje": "Usuario no autenticado",
                                      "data": null
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación — categoría no encontrada o datos incompletos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(
                                    name = "Error Categoría",
                                    value = """
                                    {
                                      "codigo": 400,
                                      "mensaje": "Una o más categorías no existen en el sistema",
                                      "data": null
                                    }
                                    """
                            )
                    )
            )
    })
    @PostMapping("/save")
    public ResponseBase<PeliculaResponse> createPelicula(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Para usar este metodo el token debe de ser proporcionado de manera obligatoria", required = true)
            @RequestBody PeliculaCreateRequest request,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseBase<>(401, "Token no proporcionado", null);
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        return peliculaService.crearPelicula(request, username);
    }


    @Operation(
            summary = "Actualizar una película existente",
            description = "Permite modificar los datos de una película registrada. Requiere autenticación mediante token JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Película actualizada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo Éxito",
                                    value = """
                                    {
                                      "codigo": 200,
                                      "mensaje": "Película actualizada correctamente",
                                      "data": {
                                        "id": 9,
                                        "nombre": "El Conjuro",
                                        "anioEstreno": 2015,
                                        "synopsis": "Una historia aterradora de demonios",
                                        "createAt": "2025-10-06T00:27:04.594+00:00",
                                        "usuarioPelicula": "eleil@gmail.com",
                                        "categorias": ["Terror"]
                                      }
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Categoría no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(
                                    name = "Error Categoría",
                                    value = """
                                    {
                                      "codigo": 400,
                                      "mensaje": "Una o más categorías no existen en el sistema",
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
                                    name = "Error Token",
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
                                    name = "Error Película",
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
    @PutMapping("/update/{id}")
    public ResponseBase<PeliculaResponse> updatePelicula(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Para usar este metodo el token debe de ser proporcionado de manera obligatoria", required = true)
            @PathVariable("id") int idPelicula,
            @RequestBody PeliculaUpdateRequest request,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseBase<>(401, "Token no proporcionado o inválido", null);
        }
        return peliculaService.actualizarPelicula(idPelicula, request);
    }


    @Operation(summary = "Eliminar una película")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Película eliminada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo delete",
                                    value = """
                                        {
                                          "codigo": 200,
                                          "mensaje": "Película eliminada correctamente",
                                          "data": null
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuario no autenticado",
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
            )
    })
    @DeleteMapping("/delete/{id}")
    public ResponseBase<String> deletePelicula(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Para usar este metodo el token debe de ser proporcionado de manera obligatoria", required = true)
            @PathVariable("id") int idPelicula) {
        return peliculaService.eliminarPelicula(idPelicula);
    }

    @Operation(summary = "Buscar películas con filtros y paginación")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de películas encontradas",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PeliculaResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuario no autenticado",
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
            )
    })
    @GetMapping("/find")
    public List<PeliculaResponse> findPelicula(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Para usar este metodo el token debe de ser proporcionado de manera obligatoria", required = true)
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(required = false) Integer anioEstreno,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String nombre,
            @RequestParam String direccion,
            @RequestParam(defaultValue = "nombre") String property
    ){
        Sort.Direction sortDirection = Sort.Direction.fromString(direccion);
        PageRequest pageRequest = PageRequest.of(page - 1, size, sortDirection, property);
        return peliculaService.findPeliculas(pageRequest, nombre, anioEstreno, categoria);
    }

    @GetMapping("/findAll")
    public List<PeliculaResponse> getAllPeliculas() {
        List<PeliculaResponse> lista = peliculaService.findAll();
        return lista;
    }

    @GetMapping("/detail/{id}")
    public List<PeliculaResponse> buscarPorId(@PathVariable("id") int id) {
        return peliculaService.buscarPorId(id);
    }

    @GetMapping("/findAllRate")
    public List<PeliculaResponseRating> getAllPeliculasRate(Authentication authentication) {
        List<PeliculaResponseRating> lista = peliculaService.findAllRate(authentication);
        return lista;
    }
}
