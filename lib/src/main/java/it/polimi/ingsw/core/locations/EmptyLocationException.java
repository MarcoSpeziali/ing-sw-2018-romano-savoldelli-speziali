package it.polimi.ingsw.core.locations;

public class EmptyLocationException extends RuntimeException {

    private static final long serialVersionUID = -7697110239504065759L;

    public EmptyLocationException(PickLocation pickLocation) {
        super(String.format("The location %s is empty", pickLocation.getClass().getName()));
    }
}
