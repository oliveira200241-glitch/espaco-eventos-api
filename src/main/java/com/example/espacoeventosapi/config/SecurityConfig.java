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
                // ==========================================
                // CORS
                // ==========================================
                .cors(Customizer.withDefaults())

                // ==========================================
                // CSRF
                // ==========================================
                .csrf(csrf -> csrf.disable())

                // ==========================================
                // DESATIVA AUTENTICAÇÕES AUTOMÁTICAS
                // ==========================================
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .logout(logout -> logout.disable())

                // ==========================================
                // API SEM SESSÃO
                // ==========================================
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // ==========================================
                // REGRAS DE ACESSO
                // ==========================================
                .authorizeHttpRequests(auth -> auth

                        // Preflight CORS
                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        // ==================================
                        // DIAGNÓSTICO:
                        // TODAS AS ROTAS DE USUÁRIOS PÚBLICAS
                        // ==================================
                        .requestMatchers(
                                "/usuarios/**"
                        ).permitAll()

                        // ==================================
                        // TODO O RESTANTE PRECISA DE JWT
                        // ==================================
                        .anyRequest().authenticated()
                )

                // ==========================================
                // TRATAMENTO DE 401
                // ==========================================
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

                // ==========================================
                // JWT
                // ==========================================
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