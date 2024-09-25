package com.solafides.blackjack_reborn.application.port.out.relational;

import com.solafides.blackjack_reborn.adapter.repository.entities.relational.GameWinners;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface GameWinnersRepository extends ReactiveCrudRepository<GameWinners, Long> {

    Mono<GameWinners> findByGameId(Long gameId);
}
