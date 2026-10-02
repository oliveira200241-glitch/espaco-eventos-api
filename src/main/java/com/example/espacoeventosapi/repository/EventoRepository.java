package com.example.espacoeventosapi.repository;

import com.example.espacoeventosapi.Evento.Evento;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventoRepository extends MongoRepository<Evento, String> {

    List<Evento> findByEspacoIdAndData(
            String espacoId,
            LocalDate data
    );
}