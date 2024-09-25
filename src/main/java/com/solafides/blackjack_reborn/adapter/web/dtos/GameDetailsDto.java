package com.solafides.blackjack_reborn.adapter.web.dtos;

import com.solafides.blackjack_reborn.adapter.repository.entities.relational.GameParticipation;
import com.solafides.blackjack_reborn.adapter.repository.entities.relational.GameWinners;
import com.solafides.blackjack_reborn.domain.GameResult;

public record GameDetailsDto(Long gameId, GameParticipation gameParticipation, GameWinners gameWinners, GameResult gameResult) {
}
