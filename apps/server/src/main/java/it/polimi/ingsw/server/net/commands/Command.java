package it.polimi.ingsw.server.net.commands;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.io.IOException;
import java.net.Socket;

// TODO: docs
public interface Command<T extends JSONSerializable, K extends JSONSerializable> {
    @SuppressWarnings("squid:S00112")
    Response<T> handle(Request<K> request, Socket client) throws IOException;

    boolean shouldBeKeptAlive();
}
