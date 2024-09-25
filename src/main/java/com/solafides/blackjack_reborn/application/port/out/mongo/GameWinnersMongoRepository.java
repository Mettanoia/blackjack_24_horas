package com.solafides.blackjack_reborn.application.port.out.mongo;

import com.solafides.blackjack_reborn.adapter.repository.entities.mongo.GameWinnersMongo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface GameWinnersMongoRepository extends ReactiveCrudRepository<GameWinnersMongo, String> {

    Mono<GameWinnersMongo> findByGameId(String gameId);
}
