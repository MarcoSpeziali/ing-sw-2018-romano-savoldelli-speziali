package it.polimi.ingsw.compilers.constraints;

public class MalformedConstraintException extends RuntimeException {
    private static final long serialVersionUID = 8619378642383128441L;

    public MalformedConstraintException(String constraintText) {
        super("The constraint \"" + constraintText + "\" is malformed.");
    }
}
