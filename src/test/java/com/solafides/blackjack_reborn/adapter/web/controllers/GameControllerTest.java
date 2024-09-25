package com.solafides.blackjack_reborn.adapter.web.controllers;

import com.solafides.blackjack_reborn.adapter.web.dtos.PlayerDetailsDto;
import com.solafides.blackjack_reborn.adapter.web.request.EditPlayerNameRequest;
import com.solafides.blackjack_reborn.adapter.web.request.GameActionRequest;
import com.solafides.blackjack_reborn.adapter.web.request.GameCreationRequest;
import com.solafides.blackjack_reborn.application.services.*;
import com.solafides.blackjack_reborn.domain.BlackjackAction;
import com.solafides.blackjack_reborn.domain.Game;
import com.solafides.blackjack_reborn.domain.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class GameControllerTest {

    private WebTestClient webTestClient;
    private GameCreation gameCreation;
    private PlayAction playAction;
    private GameDetails gameDetails;
    private EditPlayerName editPlayerName;
    private DeleteGame deleteGame;
    private GetPlayersRanking getPlayersRanking;

    @BeforeEach
    public void setUp() {
        gameCreation = Mockito.mock(GameCreation.class);
        playAction = Mockito.mock(PlayAction.class);
        gameDetails = Mockito.mock(GameDetails.class);
        editPlayerName = Mockito.mock(EditPlayerName.class);
        deleteGame = Mockito.mock(DeleteGame.class);
        getPlayersRanking = Mockito.mock(GetPlayersRanking.class);

        GameController gameController = new GameController(
                gameCreation,
                playAction,
                gameDetails,
                editPlayerName,
                deleteGame,
                getPlayersRanking
        );

        webTestClient = WebTestClient.bindToRouterFunction(gameController.gameRoutes())
                .configureClient()
                .baseUrl("http://localhost:8080")
                .build();
    }

    @Test
    public void testCreateNewGame() {
        GameCreationRequest request = new GameCreationRequest("Player1");
        Player player = new Player(1L, "Player1", "player1@example.com");
        Game game = new Game(1L, player);

        when(gameCreation.createGame(any(GameCreationRequest.class))).thenReturn(Mono.just(game));

        webTestClient.post()
                .uri("/game/new")
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("/game/1")
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.player.name").isEqualTo("Player1");
    }

    @Test
    public void testPlayAction() {

        Long gameId = 1L;
        GameActionRequest actionRequest = new GameActionRequest(BlackjackAction.HIT);
        Player player = new Player(1L, "Player1", "player1@example.com");
        Game game = new Game(gameId, player);
        game.startGame();
        game.playerHit();

        when(playAction.executeAction(any(GameActionRequest.class), any(Long.class))).thenReturn(Mono.just(game));

        webTestClient.post()
                .uri("/game/{id}/play", gameId)
                .contentType(APPLICATION_JSON)
                .bodyValue(actionRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(gameId)
                .jsonPath("$.player.hand.length()").isEqualTo(3);
    }

    @Test
    public void testEditPlayerName() {

        Long playerId = 1L;
        EditPlayerNameRequest request = new EditPlayerNameRequest("NewPlayerName");
        PlayerDetailsDto playerDetailsDto = new PlayerDetailsDto(playerId, "NewPlayerName", "player@example.com");

        when(editPlayerName.editPlayerName(playerId, "NewPlayerName")).thenReturn(Mono.just(playerDetailsDto));

        webTestClient.post()
                .uri("/player/{id}", playerId)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("NewPlayerName");

    }

    @Test
    public void testDeleteGame() {

        Long gameId = 1L;
        when(deleteGame.deleteGame(gameId)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/game/{id}/delete", gameId)
                .exchange()
                .expectStatus().isNoContent();

    }

}
