package com.solafides.blackjack_reborn.application.port.out.relational;

import com.solafides.blackjack_reborn.adapter.repository.entities.relational.GameParticipation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface GameParticipationRepository extends ReactiveCrudRepository<GameParticipation, Long> {
    Mono<GameParticipation> findAllByGameId(Long gameId);
}
