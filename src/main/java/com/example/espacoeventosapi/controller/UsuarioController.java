package com.example.espacoeventosapi.controller;

import com.example.espacoeventosapi.dto.LoginRequest;
import com.example.espacoeventosapi.service.UsuarioService;
import com.example.espacoeventosapi.usuario.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.espacoeventosapi.dto.LoginResponse;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // GET - Listar todos os usuários
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    // GET - Buscar usuário pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable String id) {

        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - Criar usuário
    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
        Usuario createdUsuario = usuarioService.criarUsuario(usuario);
        return ResponseEntity.status(201).body(createdUsuario);
    }

    // PUT - Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(
            @PathVariable String id,
            @RequestBody Usuario usuario) {

        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuario);

        return ResponseEntity.ok(usuarioAtualizado);
    }

    // DELETE - Deletar usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable String id) {

        usuarioService.deletarUsuario(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest loginRequest) {

        LoginResponse resposta = usuarioService.login(loginRequest);

        return ResponseEntity.ok(resposta);
    }
}