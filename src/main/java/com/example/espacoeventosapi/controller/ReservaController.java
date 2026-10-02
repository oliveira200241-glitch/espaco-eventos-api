package com.example.espacoeventosapi.controller;

import com.example.espacoeventosapi.reserva.Reserva;
import com.example.espacoeventosapi.service.ReservaService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<Reserva> criarReserva(
            @RequestBody Reserva reserva
    ) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Reserva novaReserva =
                reservaService.criarReserva(reserva, email);

        return ResponseEntity.ok(novaReserva);
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> listarReservas() {

        List<Reserva> reservas =
                reservaService.listarReservas();

        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> buscarPorId(
            @PathVariable String id
    ) {

        return reservaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Reserva>> listarPorUsuario(
            @PathVariable String usuarioId
    ) {

        List<Reserva> reservas =
                reservaService.listarReservasPorUsuario(usuarioId);

        return ResponseEntity.ok(reservas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> atualizarReserva(
            @PathVariable String id,
            @RequestBody Reserva reserva
    ) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Reserva reservaAtualizada =
                reservaService.atualizarReserva(
                        id,
                        reserva,
                        email
                );

        return ResponseEntity.ok(reservaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarReserva(
            @PathVariable String id
    ) {

        reservaService.deletarReserva(id);

        return ResponseEntity.noContent().build();
    }
}