package it.polimi.ingsw.compilers.actions;

// TODO: docs
public class UnrecognizedOptionalParameterException extends RuntimeException {
    private static final long serialVersionUID = 5920042569372947893L;

    public UnrecognizedOptionalParameterException(String actionId, String paramName) {
        super("Unrecognized optional parameter " + paramName + " for action " + actionId);
    }
}
