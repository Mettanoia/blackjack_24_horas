package com.solafides.blackjack_reborn.adapter.repository.entities.mongo;

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
@Document(collection = "players")
public class PlayerMongoEntity {

    @Id
    @Field("_id")
    private String id = new ObjectId().toHexString();
    private String name;
    private String email;

}
