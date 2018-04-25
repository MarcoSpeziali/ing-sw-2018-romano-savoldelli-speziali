package it.polimi.ingsw.core.locations;

public class EmptyBagException extends RuntimeException {

    public EmptyBagException(String message) {
        super(message);
    }

    public EmptyBagException() {}
}
