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

        System.out.println(
                "========== SECURITY CONFIG CARREGADO =========="
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        System.out.println(
                "========== SECURITY FILTER CHAIN CRIADA =========="
        );

        http
                // CORS
                .cors(Customizer.withDefaults())

                // CSRF não é necessário para API REST com JWT
                .csrf(csrf -> csrf.disable())

                // Desativa autenticações automáticas do Spring
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .logout(logout -> logout.disable())

                // API sem sessão
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // Regras de acesso
                .authorizeHttpRequests(auth -> auth

                        // Preflight CORS
                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        // Login público
                        .requestMatchers(
                                HttpMethod.POST,
                                "/usuarios/login"
                        ).permitAll()

                        // Cadastro público
                        .requestMatchers(
                                HttpMethod.POST,
                                "/usuarios",
                                "/usuarios/"
                        ).permitAll()

                        // Todo o restante exige JWT
                        .anyRequest().authenticated()
                )

                // Retorna 401 quando não autenticado
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(
                                (request, response, authException) -> {

                                    System.out.println(
                                            "========== 401 SECURITY =========="
                                    );

                                    System.out.println(
                                            "MÉTODO: "
                                                    + request.getMethod()
                                    );

                                    System.out.println(
                                            "URL: "
                                                    + request.getRequestURI()
                                    );

                                    response.sendError(
                                            HttpServletResponse.SC_UNAUTHORIZED
                                    );
                                }
                        )
                )

                // JWT antes do filtro padrão de usuário/senha
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        SecurityFilterChain chain = http.build();

        System.out.println(
                "========== SECURITY FILTER CHAIN PRONTA =========="
        );

        System.out.println(chain);

        return chain;
    }

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

        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}