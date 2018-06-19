package it.polimi.ingsw.utils.io.json;

public class JSONSerializationException extends RuntimeException {

    private static final long serialVersionUID = 3917758758561846942L;

    public JSONSerializationException(String message) {
        super(message);
    }

    public JSONSerializationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
