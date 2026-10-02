package com.example.espacoeventosapi.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    // ==========================================
    // ROTAS QUE NÃO PRECISAM DE JWT
    // ==========================================

    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request
    ) {

        String path = request.getServletPath();

        return path.equals("/usuarios/login")
                || path.equals("/usuarios")
                || path.equals("/usuarios/");
    }

    // ==========================================
    // FILTRO JWT
    // ==========================================

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        // ------------------------------------------
        // SEM TOKEN
        // ------------------------------------------

        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // ------------------------------------------
        // EXTRAI TOKEN
        // ------------------------------------------

        String token =
                authorizationHeader.substring(7);

        try {

            // ------------------------------------------
            // VALIDA TOKEN
            // ------------------------------------------

            if (!jwtService.tokenValido(token)) {

                SecurityContextHolder.clearContext();

                filterChain.doFilter(request, response);
                return;
            }

            // ------------------------------------------
            // EXTRAI DADOS DO TOKEN
            // ------------------------------------------

            String email =
                    jwtService.extrairEmail(token);

            String tipo =
                    jwtService.extrairTipo(token);

            // ------------------------------------------
            // DEFINE ROLE
            // ------------------------------------------

            SimpleGrantedAuthority autoridade =
                    new SimpleGrantedAuthority(
                            "ROLE_" + tipo
                    );

            // ------------------------------------------
            // CRIA AUTENTICAÇÃO
            // ------------------------------------------

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            Collections.singletonList(
                                    autoridade
                            )
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            // ------------------------------------------
            // SALVA NO SECURITY CONTEXT
            // ------------------------------------------

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

        } catch (Exception e) {

            SecurityContextHolder.clearContext();
        }

        // ------------------------------------------
        // CONTINUA REQUISIÇÃO
        // ------------------------------------------

        filterChain.doFilter(request, response);
    }
}