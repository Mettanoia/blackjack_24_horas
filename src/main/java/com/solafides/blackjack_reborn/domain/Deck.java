package com.solafides.blackjack_reborn.domain;

import java.util.*;

final class Deck {

    private Deque<Card> cards;

    public Deck() {
        initDeck();
    }

    public void initDeck() {

        cards = new ArrayDeque<>();

        for (Card.Suit suit : Card.Suit.values())
            for (Card.Rank rank : Card.Rank.values())
                cards.add(new Card(suit, rank));

    }

    public void shuffle() {

        List<Card> cardList = new ArrayList<>(cards);
        Collections.shuffle(cardList);
        cards = new ArrayDeque<>(cardList);

    }

    public Card drawCard() {
        if (cards.isEmpty())
            throw new IllegalStateException("No more cards in the deck.");


        return cards.poll();

    }

    public List<Card> drawStartingHand() {

        List<Card> startingHand = new ArrayList<>();
        startingHand.add(drawCard());
        startingHand.add(drawCard());
        return startingHand;

    }

}
