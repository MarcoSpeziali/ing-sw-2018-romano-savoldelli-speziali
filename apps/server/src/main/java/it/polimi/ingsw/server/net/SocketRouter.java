package it.polimi.ingsw.server.net;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.server.net.endpoints.SignInEndPoint;
import it.polimi.ingsw.server.net.handlers.SignInHandlers;

import java.net.Socket;

public final class SocketRouter {
    private SocketRouter() {}

    public static CommandHandler getHandlerForRequest(Request request) {
        switch (request.getRequestBody().getEndPointFunction()) {
            case REQUEST_AUTHENTICATION:
                return new SignInHandlers.RequestLoginHandler();
            case FULFILL_AUTHENTICATION_CHALLENGE:
                return new SignInHandlers.FulfillChallengeHandler();
        }

        return null;
    }
}
