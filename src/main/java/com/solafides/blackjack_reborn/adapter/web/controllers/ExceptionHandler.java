package com.solafides.blackjack_reborn.adapter.web.controllers;

import com.solafides.blackjack_reborn.application.exceptions.GameNotFoundException;
import com.solafides.blackjack_reborn.application.exceptions.PlayerNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
public class ExceptionHandler {

    public static Mono<ServerResponse> handleException(Throwable e) {

         if (e instanceof PlayerNotFoundException) {

            return status(NOT_FOUND)
                    .contentType(APPLICATION_JSON)
                    .bodyValue("{\"error\": \"Player not found: " + e.getMessage() + "\"}");

        } else if (e instanceof GameNotFoundException) {

            return status(NOT_FOUND)
                    .contentType(APPLICATION_JSON)
                    .bodyValue("{\"error\": \"Game not found: " + e.getMessage() + "\"}");

        } else {

            // De to
            return status(INTERNAL_SERVER_ERROR)
                    .contentType(APPLICATION_JSON)
                    .bodyValue("{\"error\": \"Internal Server Error: " + e.getMessage() + "\"}");

        }

    }

}
