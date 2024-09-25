package com.solafides.blackjack_reborn.application.services;

import com.solafides.blackjack_reborn.adapter.repository.entities.relational.GameEntity;
import com.solafides.blackjack_reborn.adapter.repository.entities.relational.GameWinners;
import com.solafides.blackjack_reborn.adapter.web.request.GameActionRequest;
import com.solafides.blackjack_reborn.application.config.GameStorage;
import com.solafides.blackjack_reborn.application.port.out.relational.GameRepository;
import com.solafides.blackjack_reborn.application.port.out.relational.GameWinnersRepository;
import com.solafides.blackjack_reborn.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PlayActionTest {

    private GameStorage gameStorage;
    private GameRepository gameRepository;
    private GameWinnersRepository gameWinnersRepository;
    private PlayAction playAction;

    @BeforeEach
    public void setUp() {

        gameStorage = new GameStorage();
        gameRepository = mock(GameRepository.class);
        gameWinnersRepository = mock(GameWinnersRepository.class);
        playAction = new PlayAction(gameStorage, gameRepository, gameWinnersRepository);

    }

    @Test
    public void testHitActionMutatesGameState() {

        Long gameId = 1L;
        Player player = new Player(1L, "Player1", "player1@example.com");
        Game game = new Game(gameId, player);
        gameStorage.addGame(game);
        game.startGame();

        GameActionRequest request = new GameActionRequest(BlackjackAction.HIT);

        // Test
        Mono<Game> result = playAction.executeAction(request, gameId);

        // Assert
        StepVerifier.create(result)
                .assertNext(updatedGame -> {

                    int initialHandSize = 2;
                    int newHandSize = updatedGame.getPlayer().getHand().size();

                    assert newHandSize > initialHandSize : "Player's hand should have more cards after HIT action";

                })
                .verifyComplete();

    }

    @Test
    public void testStandActionMutatesGameState() {

        Long gameId = 1L;
        Player player = new Player(1L, "Player1", "player1@example.com");
        Game game = new Game(gameId, player);
        gameStorage.addGame(game);

        game.startGame();

        GameActionRequest request = new GameActionRequest(BlackjackAction.STAND);

        when(gameRepository.save(any(GameEntity.class))).thenReturn(Mono.just(new GameEntity(gameId, GameResult.PLAYER_WON)));
        when(gameWinnersRepository.save(any(GameWinners.class))).thenReturn(Mono.empty());

        // Test
        Mono<Game> result = playAction.executeAction(request, gameId);

        // Assert
        StepVerifier.create(result)
                .assertNext(updatedGame -> {

                    int initialHandSize = 2;
                    int dealerHandSize = updatedGame.getDealer().getHand().size();

                    assert dealerHandSize > initialHandSize : "Dealer's hand should have more cards after STAND action";

                })
                .verifyComplete();
    }

}
