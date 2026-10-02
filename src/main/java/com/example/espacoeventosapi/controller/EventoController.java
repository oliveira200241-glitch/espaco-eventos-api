package com.example.espacoeventosapi.controller;

import com.example.espacoeventosapi.Evento.Evento;
import com.example.espacoeventosapi.service.EventoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping
    public ResponseEntity<Evento> criarEvento(
            @RequestBody Evento evento
    ) {
        Evento novoEvento =
                eventoService.criarEvento(evento);

        return ResponseEntity.ok(novoEvento);
    }

    @GetMapping
    public ResponseEntity<List<Evento>> listarEventos() {
        List<Evento> eventos =
                eventoService.listarEventos();

        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> buscarPorId(
            @PathVariable String id
    ) {
        return eventoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evento> atualizarEvento(
            @PathVariable String id,
            @RequestBody Evento evento
    ) {
        Evento eventoAtualizado =
                eventoService.atualizarEvento(
                        id,
                        evento
                );

        return ResponseEntity.ok(eventoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEvento(
            @PathVariable String id
    ) {
        eventoService.deletarEvento(id);

        return ResponseEntity.noContent().build();
    }
}