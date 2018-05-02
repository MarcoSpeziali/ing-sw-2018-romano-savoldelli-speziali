package it.polimi.ingsw.compilers;

// TODO: docs
public class UnrecognizedActionException extends RuntimeException {
    public UnrecognizedActionException(String actionId) {
        super("Unrecognized action " + actionId);
    }
}
