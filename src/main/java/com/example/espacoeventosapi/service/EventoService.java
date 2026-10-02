package com.example.espacoeventosapi.service;

import com.example.espacoeventosapi.Evento.Evento;
import com.example.espacoeventosapi.exception.ConflitoHorarioException;
import com.example.espacoeventosapi.repository.EventoRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public Evento criarEvento(Evento evento) {

        validarHorario(evento);

        verificarConflito(evento, null);

        return eventoRepository.save(evento);
    }

    public List<Evento> listarEventos() {
        return eventoRepository.findAll();
    }

    public Optional<Evento> buscarPorId(String id) {
        return eventoRepository.findById(id);
    }

    public Evento atualizarEvento(
            String id,
            Evento eventoAtualizado
    ) {

        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Evento não encontrado"
                        )
                );

        validarHorario(eventoAtualizado);

        verificarConflito(
                eventoAtualizado,
                id
        );

        evento.setNome(
                eventoAtualizado.getNome()
        );

        evento.setDescricao(
                eventoAtualizado.getDescricao()
        );

        evento.setData(
                eventoAtualizado.getData()
        );

        evento.setHorarioInicio(
                eventoAtualizado.getHorarioInicio()
        );

        evento.setHorarioFim(
                eventoAtualizado.getHorarioFim()
        );

        evento.setEspacoId(
                eventoAtualizado.getEspacoId()
        );

        evento.setStatus(
                eventoAtualizado.getStatus()
        );

        return eventoRepository.save(evento);
    }

    public void deletarEvento(String id) {

        if (!eventoRepository.existsById(id)) {
            throw new RuntimeException(
                    "Evento não encontrado"
            );
        }

        eventoRepository.deleteById(id);
    }

    private void validarHorario(Evento evento) {

        if (evento.getHorarioInicio() == null
                || evento.getHorarioFim() == null) {

            throw new RuntimeException(
                    "Horário de início e horário de fim são obrigatórios"
            );
        }

        if (!evento.getHorarioInicio()
                .isBefore(evento.getHorarioFim())) {

            throw new RuntimeException(
                    "O horário de início deve ser anterior ao horário de fim"
            );
        }
    }

    private void verificarConflito(
            Evento novoEvento,
            String idEventoAtual
    ) {

        List<Evento> eventosExistentes =
                eventoRepository.findByEspacoIdAndData(
                        novoEvento.getEspacoId(),
                        novoEvento.getData()
                );

        for (Evento eventoExistente : eventosExistentes) {

            if (idEventoAtual != null
                    && eventoExistente.getId()
                    .equals(idEventoAtual)) {

                continue;
            }

            boolean conflito =
                    novoEvento.getHorarioInicio()
                            .isBefore(
                                    eventoExistente.getHorarioFim()
                            )
                            &&
                            novoEvento.getHorarioFim()
                                    .isAfter(
                                            eventoExistente.getHorarioInicio()
                                    );

            if (conflito) {

                throw new ConflitoHorarioException(
                        "Já existe um evento neste espaço "
                                + "nesta data e horário"
                );
            }
        }
    }
}