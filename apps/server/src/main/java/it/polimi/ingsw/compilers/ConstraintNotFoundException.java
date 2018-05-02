package it.polimi.ingsw.compilers;

// TODO: docs
public class ConstraintNotFoundException extends RuntimeException {
    public ConstraintNotFoundException(String constraintId, String parentId) {
        super("Could not find constraint " + constraintId + ", for parent " + parentId);
    }
}
