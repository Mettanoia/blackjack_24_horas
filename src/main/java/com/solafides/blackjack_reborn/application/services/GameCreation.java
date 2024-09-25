package com.solafides.blackjack_reborn.application.services;

import com.solafides.blackjack_reborn.adapter.repository.entities.relational.GameEntity;
import com.solafides.blackjack_reborn.adapter.repository.entities.relational.GameParticipation;
import com.solafides.blackjack_reborn.adapter.repository.entities.relational.PlayerEntity;
import com.solafides.blackjack_reborn.adapter.web.request.GameCreationRequest;
import com.solafides.blackjack_reborn.application.config.GameStorage;
import com.solafides.blackjack_reborn.application.exceptions.PlayerNotFoundException;
import com.solafides.blackjack_reborn.application.port.out.relational.GameParticipationRepository;
import com.solafides.blackjack_reborn.application.port.out.relational.GameRepository;
import com.solafides.blackjack_reborn.application.port.out.relational.PlayerRepository;
import com.solafides.blackjack_reborn.domain.Game;
import com.solafides.blackjack_reborn.domain.GameResult;
import com.solafides.blackjack_reborn.domain.GameState;
import com.solafides.blackjack_reborn.domain.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.error;


@RequiredArgsConstructor
@Service
public class GameCreation {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final GameParticipationRepository participationRepository;

    private final GameStorage gameStorage; // Registry to keep all the games currently being played

    public Mono<Game> createGame(GameCreationRequest request) {

        return playerRepository.findByName(request.playerName())

                .switchIfEmpty(error(new PlayerNotFoundException(request.playerName(), true)))

                .flatMap(playerEntity -> createAndSaveGameEntity(playerEntity)
                        .flatMap(gameEntity -> createGameParticipationAndReturnGame(gameEntity, playerEntity)))

                // Side effects
                .doOnSuccess(gameStorage::addGame)
                .doOnSuccess(Game::startGame);

    }

    private Mono<GameEntity> createAndSaveGameEntity(PlayerEntity playerEntity) {

        GameEntity gameEntity = new GameEntity();
        gameEntity.setGameResult(GameResult.NONE);

        return gameRepository.save(gameEntity);

    }

    private Mono<Game> createGameParticipationAndReturnGame(GameEntity gameEntity, PlayerEntity playerEntity) {

        GameParticipation gameParticipation = new GameParticipation();

        gameParticipation.setGameId(gameEntity.getId());
        gameParticipation.setPlayerId(playerEntity.getId());

        return participationRepository.save(gameParticipation)
                .map(participation -> mapToDomainGame(gameEntity, mapToDomainPlayer(playerEntity)));

    }

    private Player mapToDomainPlayer(PlayerEntity playerEntity) {
        return new Player(
                playerEntity.getId(),
                playerEntity.getName(),
                playerEntity.getEmail()
        );
    }

    private Game mapToDomainGame(GameEntity gameEntity, Player player) {

        Game game = new Game(gameEntity.getId(), player);

        game.setGameState(gameEntity.getGameResult() == GameResult.NONE ? GameState.NOT_STARTED : GameState.GAME_OVER);
        game.setGameResult(gameEntity.getGameResult());

        return game;

    }

}
