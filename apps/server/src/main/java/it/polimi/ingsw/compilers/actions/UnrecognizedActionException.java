package it.polimi.ingsw.compilers.actions;

// TODO: docs
public class UnrecognizedActionException extends RuntimeException {
    public UnrecognizedActionException(String actionId) {
        super("Unrecognized action " + actionId);
    }
}
