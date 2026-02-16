package com.example.TareaFinal.security;

import com.example.TareaFinal.service.impl.UserDetaisServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetaisServiceImpl userDetaisService;

    public SecurityConfig(JwtFilter jwtFilter, UserDetaisServiceImpl userDetaisService) {
        this.jwtFilter = jwtFilter;
        this.userDetaisService = userDetaisService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity .cors(Customizer.withDefaults())
            httpSecurity .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/", "/index.html", "/static/**", "/css/**",
                                "/js/**", "/images/**").permitAll()
                        .requestMatchers("/api/v1/usuario/login",
                                "/api/v1/pelicula/findAll",
                                "/api/v1/pelicula/findName",
                                "/api/v1/pelicula/save",
                                "/api/v1/pelicula/update",
                                "/api/v1/pelicula/delete",
                                "/api/v1/pelicula/detail",
                                "/api/v1/categorias/**",
                                "/api/v1/usuario/delete",
                                "/api/v1/pelicula/findAllRate",
                                "/api/v1/rate/delete",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/v1/rate/save",
                                "/api/v1/rate/get",
                                "/swagger-resources/**",
                                "/webjars/**").permitAll()
                        .requestMatchers("/api/v1/rate/deleteAdmin","/api/v1/rate/findAll","/api/v1/usuario/save","/api/v1/usuario/findAll", "/api/v1/pelicula/find")
                        .hasAnyAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://proyect-movie-crud.vercel.app"
        ));

        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetaisService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
