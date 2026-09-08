package com.example.espacoeventosapi.dto;

public class UsuarioResponse {

    private String id;
    private String nome;
    private String email;
    private String telefone;

    public UsuarioResponse() {
    }

    public UsuarioResponse(
            String id,
            String nome,
            String email,
            String telefone) {

        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}