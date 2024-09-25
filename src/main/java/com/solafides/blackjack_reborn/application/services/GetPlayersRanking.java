package com.solafides.blackjack_reborn.application.services;

import com.solafides.blackjack_reborn.adapter.repository.entities.relational.GameWinners;
import com.solafides.blackjack_reborn.adapter.web.dtos.PlayerDetailsDto;
import com.solafides.blackjack_reborn.application.port.out.relational.GameWinnersRepository;
import com.solafides.blackjack_reborn.application.port.out.relational.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetPlayersRanking {

    private final GameWinnersRepository gameWinnersRepository;
    private final PlayerRepository playerRepository;

    public Mono<List<PlayerDetailsDto>> getWinnersRanking() {

        return gameWinnersRepository.findAll()

                .groupBy(GameWinners::getPlayerId)
                .flatMap(groupedFlux -> groupedFlux.count()
                        .map(count -> Tuples.of(groupedFlux.key(), count))) // <ID, count>

                // We use the count to compare
                .sort((tuple1, tuple2) -> Long.compare(tuple2.getT2(), tuple1.getT2()))

                // flatMap doesn't preserve the ordering
                .flatMapSequential(tuple -> playerRepository.findById(tuple.getT1())
                        .map(playerEntity -> new PlayerDetailsDto(
                                playerEntity.getId(),
                                playerEntity.getName(),
                                playerEntity.getEmail())))

                .collectList();

    }

}
