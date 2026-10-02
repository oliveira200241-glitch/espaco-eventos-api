package com.example.espacoeventosapi.controller;

import com.example.espacoeventosapi.espaco.Espaco;
import com.example.espacoeventosapi.service.EspacoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/espacos")
public class EspacoController {

    private final EspacoService espacoService;

    public EspacoController(EspacoService espacoService) {
        this.espacoService = espacoService;
    }

    @PostMapping
    public ResponseEntity<Espaco> criarEspaco(
            @RequestBody Espaco espaco
    ) {
        Espaco novoEspaco = espacoService.criarEspaco(espaco);

        return ResponseEntity.ok(novoEspaco);
    }

    @GetMapping
    public ResponseEntity<List<Espaco>> listarEspacos() {

        List<Espaco> espacos = espacoService.listarEspacos();

        return ResponseEntity.ok(espacos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Espaco> buscarPorId(
            @PathVariable String id
    ) {

        return espacoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Espaco> atualizarEspaco(
            @PathVariable String id,
            @RequestBody Espaco espaco
    ) {

        Espaco espacoAtualizado =
                espacoService.atualizarEspaco(id, espaco);

        return ResponseEntity.ok(espacoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEspaco(
            @PathVariable String id
    ) {

        espacoService.deletarEspaco(id);

        return ResponseEntity.noContent().build();
    }
}