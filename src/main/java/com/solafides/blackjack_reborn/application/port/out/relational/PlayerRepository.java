package com.solafides.blackjack_reborn.application.port.out.relational;

import com.solafides.blackjack_reborn.adapter.repository.entities.relational.PlayerEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PlayerRepository extends ReactiveCrudRepository<PlayerEntity, Long> {
    Mono<PlayerEntity> findByName(String name);
}
