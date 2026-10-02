package com.example.espacoeventosapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflitoHorarioException.class)
    public ResponseEntity<Map<String, String>> handleConflitoHorario(
            ConflitoHorarioException exception
    ) {

        Map<String, String> resposta = Map.of(
                "erro", "Conflito de horário",
                "mensagem", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(resposta);
    }

    @ExceptionHandler(ConflitoReservaException.class)
    public ResponseEntity<Map<String, String>> handleConflitoReserva(
            ConflitoReservaException exception
    ) {

        Map<String, String> resposta = Map.of(
                "erro", "Conflito de reserva",
                "mensagem", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(resposta);
    }
}