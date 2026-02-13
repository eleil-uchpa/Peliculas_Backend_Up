package com.example.TareaFinal.config;

import com.example.TareaFinal.entity.CategoriaEntity;
import com.example.TareaFinal.entity.RoleEntity;
import com.example.TareaFinal.entity.UsuarioEntity;
import com.example.TareaFinal.repository.CategoriaRepository;
import com.example.TareaFinal.repository.RoleRepository;
import com.example.TareaFinal.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Configuration
public class InitialConfig {
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final CategoriaRepository categoriaRepository;


    public InitialConfig(UsuarioRepository usuarioRepository, RoleRepository roleRepository, CategoriaRepository categoriaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {

            List<String> categorias = List.of(
                    "Accion", "Aventura", "Comedia", "Drama", "Ciencia Ficcion",
                    "Fantasia", "Terror", "Suspenso", "Romance", "Animacion",
                    "Documental", "Musical", "Crimen", "Historico", "Belico",
                    "Misterio", "Biografico", "Deportes", "Western", "Policial",
                    "Thriller", "Artes Marciales", "Catastrofe", "Navideno",
                    "Parodia", "Satira", "Experimental", "Cine Noir",
                    "Road Movie", "Buddy Movie", "Coming of Age", "Gangster",
                    "Epico", "Mitologico", "Psicologico", "Surrealista",
                    "Exploitation", "Found Footage", "Mockumentary",
                    "Cine de Culto", "Neo Noir", "Body Horror", "Slasher",
                    "Cyberpunk", "Steampunk", "Space Opera", "Superheroes",
                    "Zombies", "Vampiros", "Monstruos", "Distopia", "Ucronia",
                    "Alternate History", "Post Apocalipsis", "Fantasia Oscura",
                    "Fantasia Epica", "Comedia Romantica", "Comedia Negra",
                    "Comedia Dramatica", "Drama Familiar", "Drama Judicial",
                    "Drama Medico", "Drama Politico", "Drama Social",
                    "Drama Historico"
            );

            for (String nombre : categorias) {
                if (categoriaRepository.findByName(nombre).isEmpty()) {
                    CategoriaEntity categoria = new CategoriaEntity();
                    categoria.setName(nombre);
                    categoriaRepository.save(categoria);
                }
            }


            RoleEntity adminRole = roleRepository.findByRole("ADMIN")
                    .orElseGet(() -> {
                        RoleEntity role = new RoleEntity();
                        role.setRole("ADMIN");
                        return roleRepository.save(role);
                    });

            RoleEntity userRole = roleRepository.findByRole("USER")
                    .orElseGet(() -> {
                        RoleEntity role = new RoleEntity();
                        role.setRole("USER");
                        return roleRepository.save(role);
                    });

            if (!usuarioRepository.existsByCorreo("eleil@gmail.com")) {
                UsuarioEntity usuario = new UsuarioEntity();
                usuario.setCorreo("eleil@gmail.com");
                usuario.setPassword(new BCryptPasswordEncoder().encode("12345"));
                usuario.setRoleEntity(adminRole);
                usuarioRepository.save(usuario);
            }
        };
    }
}
