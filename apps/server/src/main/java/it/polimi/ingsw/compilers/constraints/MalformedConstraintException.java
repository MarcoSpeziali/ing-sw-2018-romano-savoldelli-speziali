package it.polimi.ingsw.compilers.constraints;

public class MalformedConstraintException extends RuntimeException {
    public MalformedConstraintException(String constraintText) {
        super("The constraint \"" + constraintText + "\" is malformed.");
    }
}
