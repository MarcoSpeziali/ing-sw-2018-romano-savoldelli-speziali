package it.polimi.ingsw.server.net.handlers;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.server.net.CommandHandler;
import it.polimi.ingsw.server.net.endpoints.SignUpEndPoint;

import java.net.Socket;

public class SignUpHandler implements CommandHandler {
    @Override
    public Response handle(Request request, Socket client) throws Exception {
        return SignUpEndPoint.getInstance().requestSignUp(request);
    }

    @Override
    public boolean shouldBeKeptAlive() {
        return false;
    }
}
