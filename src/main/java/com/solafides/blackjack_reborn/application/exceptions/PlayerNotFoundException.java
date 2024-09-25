package com.solafides.blackjack_reborn.application.exceptions;


public class PlayerNotFoundException extends RuntimeException {


    public PlayerNotFoundException(String message) {
        super(message);
    }

    public PlayerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayerNotFoundException(Throwable cause) {
        super(cause);
    }

    public PlayerNotFoundException(Long playerId) {
        super("Player with ID " + playerId + " not found.");
    }

    public PlayerNotFoundException(String playerName, boolean isName) {
        super("Player with name \"" + playerName + "\" not found.");
    }

}
