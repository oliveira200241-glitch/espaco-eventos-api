package com.example.espacoeventosapi.dto;

public class LoginResponse {

    private UsuarioResponse usuario;
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(UsuarioResponse usuario, String token) {
        this.usuario = usuario;
        this.token = token;
    }

    public UsuarioResponse getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioResponse usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}