package com.example.espacoeventosapi.service;

import com.example.espacoeventosapi.repository.EspacoRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspacoService {

    private final EspacoRepository espacoRepository;

    public EspacoService(EspacoRepository espacoRepository) {
        this.espacoRepository = espacoRepository;
    }

    public com.example.espacoeventosapi.espaco.Espaco criarEspaco(com.example.espacoeventosapi.espaco.Espaco espaco) {
        return espacoRepository.save(espaco);
    }

    public List<com.example.espacoeventosapi.espaco.Espaco> listarEspacos() {
        return espacoRepository.findAll();
    }

    public Optional<com.example.espacoeventosapi.espaco.Espaco> buscarPorId(String id) {
        return espacoRepository.findById(id);
    }

    public com.example.espacoeventosapi.espaco.Espaco atualizarEspaco(
            String id,
            com.example.espacoeventosapi.espaco.Espaco espacoAtualizado
    ) {

        com.example.espacoeventosapi.espaco.Espaco espaco = espacoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Espaço não encontrado"
                        )
                );

        espaco.setNome(
                espacoAtualizado.getNome()
        );

        espaco.setDescricao(
                espacoAtualizado.getDescricao()
        );

        espaco.setEndereco(
                espacoAtualizado.getEndereco()
        );

        espaco.setCapacidade(
                espacoAtualizado.getCapacidade()
        );

        espaco.setPreco(
                espacoAtualizado.getPreco()
        );

        espaco.setTipo(
                espacoAtualizado.getTipo()
        );

        espaco.setDisponibilidade(
                espacoAtualizado.getDisponibilidade()
        );

        return espacoRepository.save(espaco);
    }

    public void deletarEspaco(String id) {

        if (!espacoRepository.existsById(id)) {
            throw new RuntimeException(
                    "Espaço não encontrado"
            );
        }

        espacoRepository.deleteById(id);
    }
}