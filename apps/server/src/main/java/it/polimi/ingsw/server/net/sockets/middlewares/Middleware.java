package it.polimi.ingsw.server.net.sockets.middlewares;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.net.sockets.ClientHandler;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public interface Middleware {

    default void prepare() throws Exception {
    }

    Response handleRequest(Request<? extends JSONSerializable> request, EndPointFunction endPointFunction, ClientHandler clientHandler);

    Request handleResponse(Response<? extends JSONSerializable> response, EndPointFunction endPointFunction, ClientHandler clientHandler);

    boolean shouldBeKeptAlive();
}
