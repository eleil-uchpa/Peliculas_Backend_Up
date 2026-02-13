package com.example.TareaFinal.controller;

import com.example.TareaFinal.dto.request.LoginRequest;
import com.example.TareaFinal.dto.request.PeliculaUpdateRequest;
import com.example.TareaFinal.dto.request.UsuarioCreateRequest;
import com.example.TareaFinal.dto.response.PeliculaResponse;
import com.example.TareaFinal.dto.response.ResponseBase;
import com.example.TareaFinal.dto.response.UsuarioResponse;
import com.example.TareaFinal.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://proyect-movie-crud.vercel.app"
})
@RestController
@RequestMapping("/api/v1/usuario")
@Tag(name = "Usuario controller", description = "Controlador para la creacion y autenticacion de usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Endpoint para crear un usuario"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario creado satisfactoriamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponse.class),
                            examples = @ExampleObject(
                                    name = "Usuario creado",
                                    summary = "Ejemplo de respuesta exitosa",
                                    value = """
                                        {
                                          "userId": 4,
                                          "username": "juan13",
                                          "role": "USER"
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuario no autenticado o token inválido",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Error de autenticación",
                                    summary = "Token ausente o inválido",
                                    value = """
                                        {
                                          "error": "Token no válido o ausente"
                                        }
                                        """
                            )
                    )
            )
    })
    @PostMapping("/save")
    public UsuarioResponse createUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Para usar este metodo el token debe de ser proporcionado de manera obligatoria", required = true)
            @RequestBody UsuarioCreateRequest request
    ) {
        return usuarioService.createUser(request);
    }


    @Operation(summary = "Endpoint para obtener el token de autenticación")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token generado satisfactoriamente",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "Ejemplo de token JWT",
                                    summary = "Token válido",
                                    value = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJnZW9yZ2UxMjMiLCJpYXQiOjE3NTgzMzM1NzcsImV4cCI6MTc1ODMzNDE3N30.vFNJcJy_9dgeOl5Kt1btpS1JTDma3Y5wPxCTl58pOVM"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Error con usuario o contraseña",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Error de autenticación",
                                    summary = "Credenciales inválidas",
                                    value = "{\"error\": \"Usuario o contraseña incorrectos\"}"
                            )
                    )
            )
    })
    @PostMapping("/login")
    public String loginUser(@RequestBody LoginRequest loginRequest) {
        return usuarioService.login(loginRequest);
    }

    @GetMapping("/findAll")
    public List<UsuarioResponse> getAllUsuarios() {
        List<UsuarioResponse> lista = usuarioService.findAll();
        return lista;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseBase<String> deleteUsuarios(
           @PathVariable("id") int idPelicula) {
        return usuarioService.eliminarUsuario(idPelicula);
    }
}
