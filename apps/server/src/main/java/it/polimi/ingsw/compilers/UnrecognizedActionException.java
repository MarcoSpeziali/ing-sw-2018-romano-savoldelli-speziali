package it.polimi.ingsw.compilers;

public class UnrecognizedActionException extends RuntimeException {
    public UnrecognizedActionException(String actionId) {
        super("Unrecognized action " + actionId);
    }
}
