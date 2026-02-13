package com.example.TareaFinal.service.impl;

import com.example.TareaFinal.entity.UsuarioEntity;
import com.example.TareaFinal.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetaisServiceImpl implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public UserDetaisServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Optional<UsuarioEntity> optionalUsuarioEntity = usuarioRepository.findByCorreo(correo);
        if(optionalUsuarioEntity.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        UsuarioEntity usuarioEntity = optionalUsuarioEntity.get();
        SimpleGrantedAuthority roleUsuario = new SimpleGrantedAuthority("ROLE_" + usuarioEntity.getRoleEntity().getRole());
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(roleUsuario);

        return new User(usuarioEntity.getCorreo(), usuarioEntity.getPassword(), roles);
    }
}
