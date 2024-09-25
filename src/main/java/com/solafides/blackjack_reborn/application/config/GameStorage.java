package com.solafides.blackjack_reborn.application.config;

import com.solafides.blackjack_reborn.domain.Game;
import com.solafides.blackjack_reborn.domain.GameState;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameStorage {

    private final Map<Long, Game> gameMap = new ConcurrentHashMap<>();


    public void addGame(Game game) {
        if (game == null)
            throw new IllegalArgumentException("Game cannot be null.");

        if (game.getGameState() != GameState.NOT_STARTED)
            throw new IllegalStateException("Game must be in NOT_STARTED state to be added.");

        gameMap.put(game.getId(), game);

    }

    public Game getGame(Long gameId) {
        return gameMap.get(gameId);
    }

    public Game removeGame(Long gameId) {
        return gameMap.remove(gameId);
    }

    public boolean containsGame(Long gameId) {
        return gameMap.containsKey(gameId);
    }

    public void updateGame(Game game) {
        if (game == null || !gameMap.containsKey(game.getId()))
            throw new IllegalArgumentException("Game does not exist in storage.");

        gameMap.put(game.getId(), game);

    }

}
