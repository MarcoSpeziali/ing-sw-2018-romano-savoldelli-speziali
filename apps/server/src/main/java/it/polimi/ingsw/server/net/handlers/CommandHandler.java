package it.polimi.ingsw.server.net.handlers;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;

import java.net.Socket;

// TODO: docs
public interface CommandHandler {
    @SuppressWarnings("squid:S00112")
    Response handle(Request request, Socket client) throws Exception;

    boolean shouldBeKeptAlive();
}
