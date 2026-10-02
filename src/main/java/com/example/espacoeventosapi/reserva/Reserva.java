package com.example.espacoeventosapi.reserva;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "reservas")
public class Reserva {

    @Id
    private String id;

    private String usuarioId;

    private String espacoId;

    private LocalDate data;

    private LocalTime horarioInicio;

    private LocalTime horarioFim;

    private String status;

    public Reserva() {
    }

    public Reserva(
            String usuarioId,
            String espacoId,
            LocalDate data,
            LocalTime horarioInicio,
            LocalTime horarioFim,
            String status
    ) {
        this.usuarioId = usuarioId;
        this.espacoId = espacoId;
        this.data = data;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getEspacoId() {
        return espacoId;
    }

    public void setEspacoId(String espacoId) {
        this.espacoId = espacoId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}