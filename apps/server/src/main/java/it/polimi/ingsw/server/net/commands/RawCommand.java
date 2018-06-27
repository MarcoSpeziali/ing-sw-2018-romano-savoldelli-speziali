package it.polimi.ingsw.server.net.commands;


import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.io.IOException;
import java.net.Socket;

public interface RawCommand<T extends JSONSerializable, K extends JSONSerializable> {
    K handle(T t, Socket client) throws IOException;
    
    boolean shouldBeKeptAlive();
}