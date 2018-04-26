package it.polimi.ingsw.compilers;

public class UnrecognizedOptionalParameterException extends RuntimeException {
    public UnrecognizedOptionalParameterException(String actionId, String paramName) {
        super("Unrecognized optional parameter " + paramName + " for action " + actionId);
    }
}
