package com.example.espacoeventosapi.config;

import com.example.espacoeventosapi.service.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // ==========================================
    // PASSWORD ENCODER
    // ==========================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

    // ==========================================
    // CONFIGURAÇÃO DO SPRING SECURITY
    // ==========================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // ==========================================
                // CORS
                // ==========================================

                .cors(Customizer.withDefaults())

                // ==========================================
                // CSRF
                // ==========================================

                .csrf(csrf -> csrf.disable())

                // ==========================================
                // SESSÃO
                // ==========================================

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // ==========================================
                // AUTORIZAÇÃO DAS ROTAS
                // ==========================================

                .authorizeHttpRequests(auth -> auth

                        // ----------------------------------
                        // LOGIN
                        // ----------------------------------

                        .requestMatchers(
                                HttpMethod.POST,
                                "/usuarios/login"
                        ).permitAll()

                        // ----------------------------------
                        // CADASTRO DE USUÁRIO
                        // ----------------------------------

                        .requestMatchers(
                                HttpMethod.POST,
                                "/usuarios",
                                "/usuarios/"
                        ).permitAll()

                        // ----------------------------------
                        // TODAS AS OUTRAS ROTAS
                        // PRECISAM DE JWT
                        // ----------------------------------

                        .anyRequest().authenticated()
                )

                // ==========================================
                // ERRO DE AUTENTICAÇÃO
                // ==========================================

                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(
                                (request, response, authException) -> {

                                    response.sendError(
                                            HttpServletResponse.SC_UNAUTHORIZED
                                    );

                                }
                        )
                )

                // ==========================================
                // FILTRO JWT
                // ==========================================

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // ==========================================
    // CONFIGURAÇÃO DO CORS
    // ==========================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        "http://127.0.0.1:5500",
                        "http://localhost:5500",
                        "https://oliveira200241-glitch.github.io"
                )
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}