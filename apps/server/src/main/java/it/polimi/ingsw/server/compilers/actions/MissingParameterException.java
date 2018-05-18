package it.polimi.ingsw.server.compilers.actions;

public class MissingParameterException extends RuntimeException {

    private static final long serialVersionUID = -7837334121731656212L;

    public MissingParameterException(String actionId) {
        super("The action: " + actionId + " is missing a parameter.");
    }
}
