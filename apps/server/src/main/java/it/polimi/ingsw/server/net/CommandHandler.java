package it.polimi.ingsw.server.net;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;

import java.net.Socket;

public interface CommandHandler {
    Response handle(Request request, Socket client) throws Exception;

    boolean shouldBeKeptAlive();
}
