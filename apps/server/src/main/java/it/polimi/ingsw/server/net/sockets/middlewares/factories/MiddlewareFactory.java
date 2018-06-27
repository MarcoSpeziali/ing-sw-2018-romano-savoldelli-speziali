package it.polimi.ingsw.server.net.sockets.middlewares.factories;

import it.polimi.ingsw.server.net.sockets.middlewares.Middleware;

public interface MiddlewareFactory<T extends Middleware> {
    T instantiateMiddleware();
}
