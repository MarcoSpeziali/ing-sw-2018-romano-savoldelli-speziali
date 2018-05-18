package it.polimi.ingsw.server.compilers.actions;

// TODO: docs
public class UnrecognizedActionException extends RuntimeException {
    private static final long serialVersionUID = 7057821876019402297L;

    public UnrecognizedActionException(String actionId) {
        super("Unrecognized action " + actionId);
    }
}
