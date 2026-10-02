package com.example.espacoeventosapi.repository;

import com.example.espacoeventosapi.reserva.Reserva;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository
        extends MongoRepository<Reserva, String> {

    List<Reserva> findByEspacoIdAndData(
            String espacoId,
            LocalDate data
    );

    List<Reserva> findByUsuarioId(
            String usuarioId
    );
}