package com.solafides.blackjack_reborn.application.port.out.relational;

import com.solafides.blackjack_reborn.adapter.repository.entities.relational.GameEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface GameRepository extends ReactiveCrudRepository<GameEntity, Long> {
}
