package it.polimi.ingsw.utils.io.json;

public class JSONDeserializationException extends RuntimeException {

    private static final long serialVersionUID = -8971295551843717908L;

    public JSONDeserializationException(String message) {
        super(message);
    }

    public JSONDeserializationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
