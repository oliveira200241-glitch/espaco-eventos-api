package com.example.espacoeventosapi.espaco;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "espacos")
public class Espaco {

    @Id
    private String id;

    private String nome;
    private String descricao;
    private String endereco;
    private Integer capacidade;
    private Double preco;
    private String tipo;
    private Boolean disponibilidade;

    public Espaco() {
    }

    public Espaco(
            String nome,
            String descricao,
            String endereco,
            Integer capacidade,
            Double preco,
            String tipo,
            Boolean disponibilidade
    ) {
        this.nome = nome;
        this.descricao = descricao;
        this.endereco = endereco;
        this.capacidade = capacidade;
        this.preco = preco;
        this.tipo = tipo;
        this.disponibilidade = disponibilidade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(Boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }
}