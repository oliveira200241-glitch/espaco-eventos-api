package com.example.espacoeventosapi.Evento;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "eventos")
public class Evento {

    @Id
    private String id;

    private String nome;
    private String descricao;
    private LocalDate data;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;
    private String espacoId;
    private String status;

    public Evento() {
    }

    public Evento(
            String nome,
            String descricao,
            LocalDate data,
            LocalTime horarioInicio,
            LocalTime horarioFim,
            String espacoId,
            String status
    ) {
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.espacoId = espacoId;
        this.status = status;
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalTime horarioFim) {
        this.horarioFim = horarioFim;
    }

    public String getEspacoId() {
        return espacoId;
    }

    public void setEspacoId(String espacoId) {
        this.espacoId = espacoId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}