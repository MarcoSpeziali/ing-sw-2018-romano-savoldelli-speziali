package it.polimi.ingsw.server.net.handlers;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.net.endpoints.SignUpEndPoint;

import java.net.Socket;

@Handles(EndPointFunction.SIGN_UP)
public class SignUpHandler implements CommandHandler {

    @Override
    public Response handle(Request request, Socket client) throws Exception {
        return new SignUpEndPoint().requestSignUp(request);
    }

    @Override
    public boolean shouldBeKeptAlive() {
        return false;
    }
}
