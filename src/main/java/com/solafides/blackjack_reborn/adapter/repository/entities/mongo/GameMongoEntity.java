package com.solafides.blackjack_reborn.adapter.repository.entities.mongo;

import com.solafides.blackjack_reborn.domain.GameResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "games")
public class GameMongoEntity {

    @Id
    @Field("_id")
    private String id = new ObjectId().toHexString();
    private GameResult gameResult;

}
