package com.solafides.blackjack_reborn.domain;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
final class Card {

    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES;
    }

    @RequiredArgsConstructor
    public enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6),
        SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(10), QUEEN(10), KING(10), ACE(11);

        private final int value;

        public int getValue() {
            return value;
        }

    }

    private final Suit suit;
    private final Rank rank;
    private boolean hidden = false;

}