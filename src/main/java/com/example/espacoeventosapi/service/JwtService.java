package com.example.espacoeventosapi.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "minha-chave-secreta-para-jwt-deve-ser-bem-grande-123456789";

    private final SecretKey key = Keys.hmacShaKeyFor(
            SECRET_KEY.getBytes(StandardCharsets.UTF_8)
    );

    // Gerar token
    public String gerarToken(String email) {

        String token = Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + 86400000)
                )
                .signWith(key)
                .compact();

        System.out.println("JWT GERADO PARA: " + email);

        return token;
    }


    // Extrair email do token
    public String extrairEmail(String token) {

        String email = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        System.out.println("EMAIL EXTRAÍDO DO TOKEN: " + email);

        return email;
    }


    // Verificar se token é válido
    public boolean tokenValido(String token) {

        try {

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            System.out.println("TOKEN JWT VÁLIDO!");

            return true;

        } catch (Exception e) {

            System.out.println("TOKEN JWT INVÁLIDO!");
            System.out.println("ERRO: " + e.getMessage());

            return false;
        }
    }
}