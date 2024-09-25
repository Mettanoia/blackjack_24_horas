package com.solafides.blackjack_reborn.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public final class Player {

    private final Long id;
    private final String name;
    private final String email;

    @Setter(AccessLevel.PRIVATE)
    private List<Card> hand = new ArrayList<>();

    @Setter(AccessLevel.PRIVATE)
    private PlayerState playerState = PlayerState.PLAYING;


    public void hit(Card card) {
        if (playerState != PlayerState.PLAYING)
            throw new IllegalStateException("Cannot hit when player is not in the PLAYING state.");

        if (card == null)
            throw new IllegalArgumentException("Card cannot be null.");

        hand.add(card);

        if (isBust())
            playerState = PlayerState.BUST;
        else if (hasBlackjack())
            playerState = PlayerState.BLACKJACK;
        else if (hasTwentyOne())
            playerState = PlayerState.STAND;
    }

    public void stand() {
        if (playerState != PlayerState.PLAYING)
            throw new IllegalStateException("Cannot stand when player is not in the PLAYING state.");

        playerState = PlayerState.STAND;
    }

    public void dealStartingHand(List<Card> startingHand) {
        if (startingHand == null || startingHand.isEmpty())
            throw new IllegalArgumentException("Starting hand cannot be null or empty.");

        if (!hand.isEmpty())
            throw new IllegalStateException("Starting hand has already been dealt.");

        setHand(new ArrayList<>(startingHand)); // Defensive copy

        if (hasBlackjack())
            playerState = PlayerState.BLACKJACK;
    }

    int calculateHandValue() {

        int value = 0;
        int aceCount = 0;

        for (Card card : hand) {
            value += card.getRank().getValue();

            if (card.getRank() == Card.Rank.ACE)
                aceCount++;
        }

        // Adjust for Aces
        while (value > 21 && aceCount > 0) {
            value -= 10; // Counting Ace as 1 instead of 11
            aceCount--;
        }

        return value;
    }

    private boolean isBust() {
        return calculateHandValue() > 21;
    }

    private boolean hasTwentyOne() {
        return calculateHandValue() == 21;
    }

    private boolean hasBlackjack() {
        return hand.size() == 2 && calculateHandValue() == 21;
    }

}
