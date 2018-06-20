package it.polimi.ingsw.core.locations;

public class FullLocationException extends RuntimeException {

    private static final long serialVersionUID = -9220411322985860525L;

    public FullLocationException(PutLocation putLocation) {
        super(String.format("The location %s is full", putLocation.getClass().getName()));
    }
}
