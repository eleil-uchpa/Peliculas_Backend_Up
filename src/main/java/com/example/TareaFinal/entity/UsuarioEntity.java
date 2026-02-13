package com.example.TareaFinal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name = "usuario")
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int usuarioId;
    @Column(nullable = false, unique = true)
    private String correo;
    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_role", nullable = false)
    private RoleEntity roleEntity;


}
