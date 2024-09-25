package com.solafides.blackjack_reborn.adapter.repository.entities.relational;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("game_participation")
public class GameParticipation {

    @Id
    private Long id;

    @Column("game_id")
    private Long gameId;

    @Column("player_id")
    private Long playerId;

}
