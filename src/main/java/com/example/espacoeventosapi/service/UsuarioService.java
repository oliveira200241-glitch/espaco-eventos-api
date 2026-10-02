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

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario criarUsuario(Usuario usuario) {

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException(
                    "Este email já está cadastrado"
            );
        }

        if (usuario.getDocumento() != null
                && !usuario.getDocumento().isBlank()
                && usuarioRepository.existsByDocumento(
                usuario.getDocumento()
        )) {

            throw new RuntimeException(
                    "Este CPF/CNPJ já está cadastrado"
            );
        }

        usuario.setSenha(
                passwordEncoder.encode(usuario.getSenha())
        );

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorId(String id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

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

    public void deletarUsuario(String id) {

        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException(
                    "Usuário não encontrado"
            );
        }

        usuarioRepository.deleteById(id);
    }

    public LoginResponse login(LoginRequest loginRequest) {

        String identificador =
                loginRequest.getIdentificador();

        Usuario usuario;

        if (identificador == null
                || identificador.isBlank()) {

            throw new RuntimeException(
                    "Email ou CPF/CNPJ é obrigatório"
            );
        }

        if (identificador.contains("@")) {

            usuario = usuarioRepository
                    .findByEmail(identificador)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Email ou senha inválidos"
                            )
                    );

        } else {

            usuario = usuarioRepository
                    .findByDocumento(identificador)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "CPF/CNPJ ou senha inválidos"
                            )
                    );
        }

        boolean senhaCorreta =
                passwordEncoder.matches(
                        loginRequest.getSenha(),
                        usuario.getSenha()
                );

        if (!senhaCorreta) {

            throw new RuntimeException(
                    "Email/CPF/CNPJ ou senha inválidos"
            );
        }

        UsuarioResponse usuarioResponse =
                converterParaResponse(usuario);

        String token =
                jwtService.gerarToken(
                        usuario.getEmail(),
                        usuario.getTipo()
                );

        return new LoginResponse(
                usuarioResponse,
                token
        );
    }

    private UsuarioResponse converterParaResponse(
            Usuario usuario
    ) {

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getTipo()
        );
    }
}