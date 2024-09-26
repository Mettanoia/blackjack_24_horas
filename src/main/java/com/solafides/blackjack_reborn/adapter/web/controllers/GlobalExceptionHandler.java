package com.solafides.blackjack_reborn.adapter.web.controllers;

import com.solafides.blackjack_reborn.application.exceptions.GameNotFoundException;
import com.solafides.blackjack_reborn.application.exceptions.PlayerNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public Mono<ResponseEntity<String>> handleGameNotFoundException(GameNotFoundException ex) {
        return Mono.just(status(NOT_FOUND)
                .body("Game not found. Detailed message: " + ex.getMessage()));
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public Mono<ResponseEntity<String>> handlePlayerNotFoundException(PlayerNotFoundException ex) {
        return Mono.just(status(NOT_FOUND)
                .body("Player not found. Detailed message: " + ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public Mono<ResponseEntity<String>> handleRuntimeException(RuntimeException ex) {
        return Mono.just(ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body("Exception caught: " + ex.getMessage()));
    }

}
