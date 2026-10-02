package com.example.espacoeventosapi.service;

import com.example.espacoeventosapi.exception.ConflitoReservaException;
import com.example.espacoeventosapi.espaco.Espaco;
import com.example.espacoeventosapi.Evento.Evento;
import com.example.espacoeventosapi.reserva.Reserva;
import com.example.espacoeventosapi.repository.EspacoRepository;
import com.example.espacoeventosapi.repository.EventoRepository;
import com.example.espacoeventosapi.repository.ReservaRepository;
import com.example.espacoeventosapi.repository.UsuarioRepository;
import com.example.espacoeventosapi.usuario.Usuario;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EspacoRepository espacoRepository;
    private final EventoRepository eventoRepository;

    public ReservaService(
            ReservaRepository reservaRepository,
            UsuarioRepository usuarioRepository,
            EspacoRepository espacoRepository,
            EventoRepository eventoRepository
    ) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.espacoRepository = espacoRepository;
        this.eventoRepository = eventoRepository;
    }

    public Reserva criarReserva(
            Reserva reserva,
            String emailUsuario
    ) {

        Usuario usuario = usuarioRepository
                .findByEmail(emailUsuario)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuário autenticado não encontrado"
                        ));

        Espaco espaco = espacoRepository
                .findById(reserva.getEspacoId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Espaço não encontrado"
                        ));

        if (Boolean.FALSE.equals(
                espaco.getDisponibilidade()
        )) {
            throw new RuntimeException(
                    "Este espaço não está disponível"
            );
        }

        if (reserva.getData() == null) {
            throw new RuntimeException(
                    "A data da reserva é obrigatória"
            );
        }

        if (reserva.getData().isBefore(LocalDate.now())) {
            throw new RuntimeException(
                    "A data da reserva não pode estar no passado"
            );
        }

        validarHorario(reserva);

        verificarConflitoComReservas(
                reserva,
                null
        );

        verificarConflitoComEventos(reserva);

        // O usuário vem do JWT,
        // e não do JSON enviado pelo cliente.
        reserva.setUsuarioId(usuario.getId());

        reserva.setStatus("PENDENTE");

        return reservaRepository.save(reserva);
    }

    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    public List<Reserva> listarReservasPorUsuario(
            String usuarioId
    ) {

        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RuntimeException(
                    "Usuário não encontrado"
            );
        }

        return reservaRepository.findByUsuarioId(
                usuarioId
        );
    }

    public Optional<Reserva> buscarPorId(String id) {
        return reservaRepository.findById(id);
    }

    public Reserva atualizarReserva(
            String id,
            Reserva reservaAtualizada,
            String emailUsuario
    ) {

        Reserva reserva = reservaRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Reserva não encontrada"
                        ));

        Usuario usuario = usuarioRepository
                .findByEmail(emailUsuario)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuário autenticado não encontrado"
                        ));

        Espaco espaco = espacoRepository
                .findById(
                        reservaAtualizada.getEspacoId()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Espaço não encontrado"
                        ));

        if (Boolean.FALSE.equals(
                espaco.getDisponibilidade()
        )) {
            throw new RuntimeException(
                    "Este espaço não está disponível"
            );
        }

        if (reservaAtualizada.getData() == null) {
            throw new RuntimeException(
                    "A data da reserva é obrigatória"
            );
        }

        if (reservaAtualizada.getData()
                .isBefore(LocalDate.now())) {

            throw new RuntimeException(
                    "A data da reserva não pode estar no passado"
            );
        }

        validarHorario(reservaAtualizada);

        verificarConflitoComReservas(
                reservaAtualizada,
                id
        );

        verificarConflitoComEventos(
                reservaAtualizada
        );

        // O usuário também vem do JWT
        // durante a atualização.
        reserva.setUsuarioId(
                usuario.getId()
        );

        reserva.setEspacoId(
                reservaAtualizada.getEspacoId()
        );

        reserva.setData(
                reservaAtualizada.getData()
        );

        reserva.setHorarioInicio(
                reservaAtualizada.getHorarioInicio()
        );

        reserva.setHorarioFim(
                reservaAtualizada.getHorarioFim()
        );

        reserva.setStatus(
                reservaAtualizada.getStatus()
        );

        return reservaRepository.save(reserva);
    }

    public void deletarReserva(String id) {

        if (!reservaRepository.existsById(id)) {
            throw new RuntimeException(
                    "Reserva não encontrada"
            );
        }

        reservaRepository.deleteById(id);
    }

    private void validarHorario(Reserva reserva) {

        if (reserva.getHorarioInicio() == null
                || reserva.getHorarioFim() == null) {

            throw new RuntimeException(
                    "Horário de início e horário de fim são obrigatórios"
            );
        }

        if (!reserva.getHorarioInicio()
                .isBefore(reserva.getHorarioFim())) {

            throw new RuntimeException(
                    "O horário de início deve ser anterior ao horário de fim"
            );
        }
    }

    private void verificarConflitoComReservas(
            Reserva novaReserva,
            String idReservaAtual
    ) {

        List<Reserva> reservasExistentes =
                reservaRepository
                        .findByEspacoIdAndData(
                                novaReserva.getEspacoId(),
                                novaReserva.getData()
                        );

        for (Reserva reservaExistente :
                reservasExistentes) {

            if (idReservaAtual != null
                    && reservaExistente.getId()
                    .equals(idReservaAtual)) {

                continue;
            }

            if ("CANCELADA".equalsIgnoreCase(
                    reservaExistente.getStatus()
            )) {
                continue;
            }

            boolean conflito =
                    novaReserva.getHorarioInicio()
                            .isBefore(
                                    reservaExistente
                                            .getHorarioFim()
                            )
                            &&
                            novaReserva.getHorarioFim()
                                    .isAfter(
                                            reservaExistente
                                                    .getHorarioInicio()
                                    );

            if (conflito) {

                throw new ConflitoReservaException(
                        "Já existe uma reserva neste espaço nesta data e horário"
                );
            }
        }
    }

    private void verificarConflitoComEventos(
            Reserva reserva
    ) {

        List<Evento> eventosExistentes =
                eventoRepository
                        .findByEspacoIdAndData(
                                reserva.getEspacoId(),
                                reserva.getData()
                        );

        for (Evento evento :
                eventosExistentes) {

            if ("CANCELADO".equalsIgnoreCase(
                    evento.getStatus()
            )) {
                continue;
            }

            boolean conflito =
                    reserva.getHorarioInicio()
                            .isBefore(
                                    evento.getHorarioFim()
                            )
                            &&
                            reserva.getHorarioFim()
                                    .isAfter(
                                            evento.getHorarioInicio()
                                    );

            if (conflito) {

                throw new ConflitoReservaException(
                        "Já existe um evento neste espaço nesta data e horário"
                );
            }
        }
    }
}