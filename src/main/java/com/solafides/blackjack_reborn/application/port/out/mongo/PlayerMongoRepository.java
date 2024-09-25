package com.solafides.blackjack_reborn.application.port.out.mongo;

import com.solafides.blackjack_reborn.adapter.repository.entities.mongo.PlayerMongoEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PlayerMongoRepository extends ReactiveCrudRepository<PlayerMongoEntity, String> {
    Mono<PlayerMongoEntity> findByName(String name);
}
