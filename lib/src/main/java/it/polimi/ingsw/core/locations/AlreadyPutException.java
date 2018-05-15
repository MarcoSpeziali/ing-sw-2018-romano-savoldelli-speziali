package it.polimi.ingsw.core.locations;

public class AlreadyPutException extends RuntimeException {

    private static final long serialVersionUID = -9220411322985860525L;

    public AlreadyPutException(String message) {
        super(message);
    }

    public AlreadyPutException() {}
}
