package com.solafides.blackjack_reborn.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

public final class Game {

    @Getter
    private final Long id;

    @Getter
    private final Player player;

    @Getter
    private final Player dealer;

    @JsonIgnore
    private final Deck deck;

    @Getter
    @Setter
    private GameState gameState = GameState.NOT_STARTED;

    @Getter
    @Setter
    private GameResult gameResult = GameResult.NONE;

    public Game(Long id, Player player) {
        this.id = id;
        this.player = player;
        this.dealer = new Player(0L, "Dealer", "dealer@casino.com");
        this.deck = new Deck();
    }

    public void startGame() {
        if (gameState != GameState.NOT_STARTED)
            throw new IllegalStateException("Game has already started.");

        deck.shuffle();

        player.dealStartingHand(deck.drawStartingHand());
        dealer.dealStartingHand(deck.drawStartingHand());

        dealer.getHand().get(0).setHidden(true); // One card is face down

        // Check for initial blackjack
        if (player.getPlayerState() == PlayerState.BLACKJACK) {

            gameResult = dealer.getPlayerState() == PlayerState.BLACKJACK ?
                    GameResult.PUSH :
                    GameResult.PLAYER_WON;

            gameState = GameState.GAME_OVER;

        } else
            gameState = GameState.PLAYER_TURN;

    }

    public void playerHit() {
        if (gameState != GameState.PLAYER_TURN)
            throw new IllegalStateException("It's not the player's turn.");

        Card card = deck.drawCard();
        player.hit(card);

        if (player.getPlayerState() == PlayerState.BUST) {
            gameResult = GameResult.DEALER_WON;
            gameState = GameState.GAME_OVER;
        } else if (player.getPlayerState() == PlayerState.BLACKJACK || player.getPlayerState() == PlayerState.STAND) {
            gameState = GameState.DEALER_TURN;
            dealerTurn();
        }

    }

    public void playerStand() {
        if (gameState != GameState.PLAYER_TURN)
            throw new IllegalStateException("It's not the player's turn.");

        player.stand();
        gameState = GameState.DEALER_TURN;
        dealerTurn();

    }

    private void dealerTurn() {

        while (dealer.calculateHandValue() < 17) {
            Card card = deck.drawCard();
            dealer.hit(card);
        }


        determineGameResult();
        gameState = GameState.GAME_OVER;

    }

    private void determineGameResult() {

        int playerValue = player.calculateHandValue();
        int dealerValue = dealer.calculateHandValue();

        if (dealer.getPlayerState() == PlayerState.BUST) {
            gameResult = GameResult.PLAYER_WON;
        } else if (playerValue > dealerValue) {
            gameResult = GameResult.PLAYER_WON;
        } else if (playerValue < dealerValue) {
            gameResult = GameResult.DEALER_WON;
        } else {
            gameResult = GameResult.PUSH;
        }

    }

}
