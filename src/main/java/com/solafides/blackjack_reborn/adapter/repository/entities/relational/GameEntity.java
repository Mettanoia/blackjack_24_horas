package com.solafides.blackjack_reborn.adapter.repository.entities.relational;

import com.solafides.blackjack_reborn.domain.GameResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("games")
public class GameEntity {

    @Id
    private Long id;
    private GameResult gameResult;

}
