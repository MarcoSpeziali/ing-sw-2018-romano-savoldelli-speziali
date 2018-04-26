package it.polimi.ingsw.compilers;

public class ConstraintNotFoundException extends RuntimeException {
    public ConstraintNotFoundException(String constraintId, String parentId) {
        super("Could not find constraint " + constraintId + ", for parent " + parentId);
    }
}
