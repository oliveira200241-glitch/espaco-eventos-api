package com.example.espacoeventosapi.service;

import com.example.espacoeventosapi.dto.LoginRequest;
import com.example.espacoeventosapi.dto.LoginResponse;
import com.example.espacoeventosapi.dto.UsuarioResponse;
import com.example.espacoeventosapi.repository.UsuarioRepository;
import com.example.espacoeventosapi.usuario.Usuario;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // Listar todos os usuários
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Criar usuário
    public Usuario criarUsuario(Usuario usuario) {

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException(
                    "Este email já está cadastrado"
            );
        }

        // Criptografa a senha antes de salvar
        usuario.setSenha(
                passwordEncoder.encode(usuario.getSenha())
        );

        return usuarioRepository.save(usuario);
    }

    // Buscar usuário por ID
    public Optional<Usuario> buscarPorId(String id) {
        return usuarioRepository.findById(id);
    }

    // Atualizar usuário
    public Usuario atualizarUsuario(
            String id,
            Usuario usuarioAtualizado
    ) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuário não encontrado"
                        )
                );

        usuario.setNome(usuarioAtualizado.getNome());
        usuario.setEmail(usuarioAtualizado.getEmail());
        usuario.setTelefone(usuarioAtualizado.getTelefone());

        return usuarioRepository.save(usuario);
    }

    // Deletar usuário
    public void deletarUsuario(String id) {

        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException(
                    "Usuário não encontrado"
            );
        }

        usuarioRepository.deleteById(id);
    }

    // Login
    public LoginResponse login(LoginRequest loginRequest) {

        Usuario usuario = usuarioRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Email ou senha inválidos"
                        )
                );

        // Verifica se a senha informada corresponde
        // à senha criptografada no banco
        boolean senhaCorreta = passwordEncoder.matches(
                loginRequest.getSenha(),
                usuario.getSenha()
        );

        if (!senhaCorreta) {
            throw new RuntimeException(
                    "Email ou senha inválidos"
            );
        }

        // Converte o usuário para resposta
        UsuarioResponse usuarioResponse =
                converterParaResponse(usuario);

        // Gera o token JWT
        String token = jwtService.gerarToken(
                usuario.getEmail()
        );

        return new LoginResponse(
                usuarioResponse,
                token
        );
    }

    // Converter Usuario para UsuarioResponse
    private UsuarioResponse converterParaResponse(
            Usuario usuario
    ) {

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone()
        );
    }
}