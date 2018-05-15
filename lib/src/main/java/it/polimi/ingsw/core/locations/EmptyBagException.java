package it.polimi.ingsw.core.locations;

public class EmptyBagException extends RuntimeException {

    private static final long serialVersionUID = -7697110239504065759L;

    public EmptyBagException(String message) {
        super(message);
    }

    public EmptyBagException() {}
}
