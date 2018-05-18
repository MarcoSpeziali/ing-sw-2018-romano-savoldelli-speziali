package it.polimi.ingsw.server.compilers.actions;

// TODO: docs
public class ConstraintNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -7945173417505545377L;

    public ConstraintNotFoundException(String constraintId) {
        super("Could not find constraint " + constraintId);
    }
}
