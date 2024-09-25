package com.solafides.blackjack_reborn.application.services;

import com.solafides.blackjack_reborn.adapter.repository.entities.relational.GameEntity;
import com.solafides.blackjack_reborn.adapter.repository.entities.relational.GameWinners;
import com.solafides.blackjack_reborn.adapter.web.request.GameActionRequest;
import com.solafides.blackjack_reborn.application.config.GameStorage;
import com.solafides.blackjack_reborn.application.exceptions.GameNotFoundException;
import com.solafides.blackjack_reborn.application.port.out.relational.GameRepository;
import com.solafides.blackjack_reborn.application.port.out.relational.GameWinnersRepository;
import com.solafides.blackjack_reborn.domain.BlackjackAction;
import com.solafides.blackjack_reborn.domain.Game;
import com.solafides.blackjack_reborn.domain.GameResult;
import com.solafides.blackjack_reborn.domain.GameState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.error;

@RequiredArgsConstructor
@Service
public class PlayAction {

    private final GameStorage gameStorage;
    private final GameRepository gameRepository;
    private final GameWinnersRepository gameWinnersRepository;

    public Mono<Game> executeAction(GameActionRequest request, Long gameId) {

        return Mono.justOrEmpty(gameStorage.getGame(gameId))
                .switchIfEmpty(error(new GameNotFoundException(gameId)))
                .flatMap(game -> {
                    try {
                        // Start the game if it hasn't started yet
                        if (game.getGameState() == GameState.NOT_STARTED) {
                            game.startGame();
                        }

                        performAction(game, request.getAction());

                        gameStorage.updateGame(game);

                        if (game.getGameState() == GameState.GAME_OVER) {
                            // Save the game result
                            return gameRepository.save(new GameEntity(game.getId(), game.getGameResult()))
                                    .flatMap(savedGameEntity -> {
                                        if (game.getGameResult() == GameResult.PLAYER_WON) {
                                            // Save the winner
                                            return gameWinnersRepository.save(new GameWinners(null, game.getId(), game.getPlayer().getId()))
                                                    .thenReturn(game);
                                        } else {
                                            return Mono.just(game);
                                        }
                                    });
                        } else {
                            return Mono.just(game);
                        }

                    } catch (IllegalStateException | IllegalArgumentException e) {
                        return error(e);
                    }

                });

    }

    private void performAction(Game game, BlackjackAction action) {

        switch (action) {

            case HIT:
                game.playerHit();
                break;

            case STAND:
                game.playerStand();
                break;

            default:
                throw new UnsupportedOperationException("Unknown action: " + action);

        }

    }

}
