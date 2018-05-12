package it.polimi.ingsw.compilers.actions;

// TODO: docs
public class ConstraintNotFoundException extends RuntimeException {
    public ConstraintNotFoundException(String constraintId) {
        super("Could not find constraint " + constraintId);
    }
}
