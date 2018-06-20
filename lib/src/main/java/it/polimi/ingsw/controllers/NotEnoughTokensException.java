package it.polimi.ingsw.controllers;

public class NotEnoughTokensException extends Exception {

    private static final long serialVersionUID = 1738462148316785801L;

    private final int requiredTokens;
    private final int currentTokensCount;

    public NotEnoughTokensException(int requiredTokens, int currentTokensCount) {
        super(String.format("Not enough tokens to activate the tool card, it requires %d tokens yet the player has only %d tokens", requiredTokens, currentTokensCount));

        this.requiredTokens = requiredTokens;
        this.currentTokensCount = currentTokensCount;
    }

    public int getRequiredTokens() {
        return requiredTokens;
    }

    public int getCurrentTokensCount() {
        return currentTokensCount;
    }
}
