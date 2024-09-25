package com.solafides.blackjack_reborn.application.exceptions;


public class GameNotFoundException extends RuntimeException {


    public GameNotFoundException(String message) {
        super(message);
    }

    public GameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameNotFoundException(Throwable cause) {
        super(cause);
    }

    public GameNotFoundException(Long gameId) {
        super("Game with ID " + gameId + " not found.");
    }

}
