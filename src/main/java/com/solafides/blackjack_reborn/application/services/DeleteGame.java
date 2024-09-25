package com.solafides.blackjack_reborn.application.services;

import com.solafides.blackjack_reborn.application.port.out.relational.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeleteGame {

    private final GameRepository gameRepository;

    public Mono<Void> deleteGame(Long gameId) {

        return gameRepository.deleteById(gameId);

    }

}
