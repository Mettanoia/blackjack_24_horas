package com.solafides.blackjack_reborn.adapter.web.controllers;

import com.solafides.blackjack_reborn.adapter.web.dtos.PlayerDetailsDto;
import com.solafides.blackjack_reborn.adapter.web.request.EditPlayerNameRequest;
import com.solafides.blackjack_reborn.adapter.web.request.GameActionRequest;
import com.solafides.blackjack_reborn.adapter.web.request.GameCreationRequest;
import com.solafides.blackjack_reborn.application.services.*;
import com.solafides.blackjack_reborn.domain.Game;
import com.solafides.blackjack_reborn.adapter.web.dtos.GameDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static reactor.core.publisher.Mono.just;


@Configuration
@RequiredArgsConstructor
public class GameController {

    private final GameCreation gameCreation;
    private final PlayAction playAction;
    private final GameDetails gameDetails;
    private final EditPlayerName editPlayerName;
    private final DeleteGame deleteGame;
    private final GetPlayersRanking getPlayersRanking;

    @Bean
    public RouterFunction<ServerResponse> gameRoutes() {

        return route(POST("/game/new"), this::createNewGame)
                .andRoute(POST("/game/{id}/play"), this::playAction)
                .andRoute(GET("/game/{id}"), this::getGameDetails)
                .andRoute(POST("/player/{id}"), this::editPlayerName)
                .andRoute(DELETE("/game/{id}/delete"), this::deleteGame)
                .andRoute(GET("/ranking"), this::getPlayersRanking);

    }

    private Mono<ServerResponse> getPlayersRanking(ServerRequest request) {

        return getPlayersRanking.getWinnersRanking()
                .flatMap(playerDetailsDtos -> ok().body(just(playerDetailsDtos), PlayerDetailsDto.class));

    }

    private Mono<ServerResponse> deleteGame(ServerRequest request) {

        return deleteGame.deleteGame(Long.valueOf(request.pathVariable("id")))

                .then(noContent().build());

                // Captures any error
                //.onErrorResume(ExceptionHandler::handleException);

    }

    private Mono<ServerResponse> editPlayerName(ServerRequest request) {

        return request.bodyToMono(EditPlayerNameRequest.class)
                .flatMap(editPlayerNameRequest -> editPlayerName.editPlayerName(Long.valueOf(request.pathVariable("id")), editPlayerNameRequest.newPlayerName()))
                .flatMap(playerDetailsDto -> ok().body(just(playerDetailsDto), PlayerDetailsDto.class));

                // Captures any error
                //.onErrorResume(ExceptionHandler::handleException);

    }

    private Mono<ServerResponse> getGameDetails(ServerRequest request) {

        return gameDetails.getGameDetails(Long.valueOf(request.pathVariable("id")))
                .flatMap(gameDetailsDto -> ok().body(just(gameDetailsDto), GameDetailsDto.class));

                // Captures any error
                //.onErrorResume(ExceptionHandler::handleException);

    }

    public Mono<ServerResponse> playAction(ServerRequest request) {

        Mono<GameActionRequest> actionRequestMono = request.bodyToMono(GameActionRequest.class);

        return actionRequestMono
                .flatMap(gameActionRequest -> playAction.executeAction(gameActionRequest, Long.valueOf(request.pathVariable("id"))))
                .flatMap(game -> ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(game));

                // Captures any error
                //.onErrorResume(ExceptionHandler::handleException);

    }

    private Mono<ServerResponse> createNewGame(ServerRequest request) {

        return request.bodyToMono(GameCreationRequest.class)
                .flatMap(gameCreation::createGame)
                .flatMap(this::buildCreatedResponse);

                // Captures any error
                //.onErrorResume(ExceptionHandler::handleException);

    }

    private Mono<ServerResponse> buildCreatedResponse(Game game) {

        return ServerResponse.created(URI.create("/game/" + game.getId()))
                .body(just(game), Game.class);

    }

}
