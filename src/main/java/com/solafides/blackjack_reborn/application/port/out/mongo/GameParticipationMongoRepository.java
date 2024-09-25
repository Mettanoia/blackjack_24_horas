package com.solafides.blackjack_reborn.application.port.out.mongo;

import com.solafides.blackjack_reborn.adapter.repository.entities.mongo.GameParticipationMongo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface GameParticipationMongoRepository extends ReactiveCrudRepository<GameParticipationMongo, Long> {
    Mono<GameParticipationMongo> findAllByGameId(String gameId);
}
