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
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static reactor.core.publisher.Mono.just;

public class GameCreationTest {

    private final GameRepository gameRepository =
            mock(GameRepository.class);

    private final PlayerRepository playerRepository =
            mock(PlayerRepository.class);

    private final GameParticipationRepository participationRepository =
            mock(GameParticipationRepository.class);

    private final GameCreation gameCreation =
            new GameCreation(gameRepository, playerRepository, participationRepository, new GameStorage());

    @Test
    public void createGame_shouldCreateGameSuccessfully() {

        PlayerEntity playerEntity =
                new PlayerEntity(1L, "John Doe", "john@example.com");

        GameEntity gameEntity =
                new GameEntity(1L, GameResult.NONE);

        GameParticipation gameParticipation =
                new GameParticipation(1L, 1L, 1L);


        when(playerRepository.findByName("John Doe"))
                .thenReturn(just(playerEntity));

        when(gameRepository.save(any(GameEntity.class)))
                .thenReturn(just(gameEntity));

        when(participationRepository.save(any(GameParticipation.class)))
                .thenReturn(just(gameParticipation));


        // Test
        Mono<Game> result = gameCreation.createGame(new GameCreationRequest("John Doe"));

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(game -> game.getPlayer().getName().equals("John Doe") && game.getPlayer().getId().equals(1L))
                .verifyComplete();

        verify(playerRepository).findByName("John Doe");
        verify(gameRepository).save(any(GameEntity.class));
        verify(participationRepository).save(any(GameParticipation.class));

    }

    @Test
    public void createGame_shouldThrowPlayerNotFoundException() {

        // No player
        when(playerRepository.findByName("Unknown"))
                .thenReturn(Mono.empty());

        // Test
        Mono<Game> result = gameCreation.createGame(new GameCreationRequest("Unknown"));

        // Assert
        StepVerifier.create(result)
                .expectError(PlayerNotFoundException.class)
                .verify();

        verify(playerRepository).findByName("Unknown");
        verifyNoInteractions(gameRepository);
        verifyNoInteractions(participationRepository);

    }

}
