package com.solafides.blackjack_reborn.application.services;

import com.solafides.blackjack_reborn.adapter.repository.entities.relational.GameWinners;
import com.solafides.blackjack_reborn.application.exceptions.GameNotFoundException;
import com.solafides.blackjack_reborn.application.port.out.relational.GameParticipationRepository;
import com.solafides.blackjack_reborn.application.port.out.relational.GameRepository;
import com.solafides.blackjack_reborn.application.port.out.relational.GameWinnersRepository;
import com.solafides.blackjack_reborn.adapter.web.dtos.GameDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

@RequiredArgsConstructor
@Service
public class GameDetails {

    private final GameRepository gameRepository;
    private final GameParticipationRepository gameParticipationRepository;
    private final GameWinnersRepository gameWinnersRepository;

    public Mono<GameDetailsDto> getGameDetails(Long gameId) {

        return gameRepository.findById(gameId)

                .switchIfEmpty(error(new GameNotFoundException(gameId)))

                .flatMap(gameEntity -> gameParticipationRepository.findAllByGameId(gameEntity.getId())

                        .switchIfEmpty(error(new GameNotFoundException("Game entity for Game with ID " + gameId + " not found.")))

                        .flatMap(gameParticipation -> gameWinnersRepository.findByGameId(gameEntity.getId())

                                // There may be no winners
                                .switchIfEmpty(just(new GameWinners(null, gameId, null)))

                                .map(gameWinners -> new GameDetailsDto(gameEntity.getId(), gameParticipation, gameWinners.getId() == null ? null : gameWinners, gameEntity.getGameResult()))));

    }

}
