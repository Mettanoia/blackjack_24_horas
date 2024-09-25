package com.solafides.blackjack_reborn.application.services;

import com.solafides.blackjack_reborn.adapter.repository.entities.relational.PlayerEntity;
import com.solafides.blackjack_reborn.adapter.web.dtos.PlayerDetailsDto;
import com.solafides.blackjack_reborn.application.exceptions.PlayerNotFoundException;
import com.solafides.blackjack_reborn.application.port.out.relational.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.error;

@RequiredArgsConstructor
@Service
public class EditPlayerName {

    private final PlayerRepository playerRepository;

    public Mono<PlayerDetailsDto> editPlayerName(Long playerId, String playerName) {

        return playerRepository.findById(playerId)

                // If Player doesn't exist
                .switchIfEmpty(error(new PlayerNotFoundException(playerId)))

                .map(playerEntity -> new PlayerEntity(playerEntity.getId(), playerName, playerEntity.getEmail()))
                .flatMap(playerEntity -> playerRepository.save(playerEntity))
                .map(playerEntity -> new PlayerDetailsDto(playerEntity.getId(), playerEntity.getName(), playerEntity.getEmail()));

    }

}
