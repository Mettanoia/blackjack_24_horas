package com.solafides.blackjack_reborn.application.port.out.mongo;

import com.solafides.blackjack_reborn.adapter.repository.entities.mongo.GameMongoEntity;
import com.solafides.blackjack_reborn.adapter.repository.entities.relational.GameEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface GameMongoRepository extends ReactiveCrudRepository<GameMongoEntity, String> {
}
