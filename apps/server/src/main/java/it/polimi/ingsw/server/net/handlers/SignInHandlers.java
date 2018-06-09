package it.polimi.ingsw.server.net.handlers;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.net.endpoints.SignInEndPoint;

import java.net.Socket;

public class SignInHandlers {
    private SignInHandlers() {}

    @Handles(EndPointFunction.SIGN_IN_REQUEST_AUTHENTICATION)
    public static class RequestLoginHandler implements CommandHandler {

        @Override
        public Response handle(Request request, Socket client) throws Exception {
            SignInEndPoint signInEndPoint = new SignInEndPoint();
            signInEndPoint.setSocket(client);

            return signInEndPoint.requestLogin(request);
        }

        @Override
        public boolean shouldBeKeptAlive() {
            return false;
        }
    }

    @Handles(EndPointFunction.SIGN_IN_FULFILL_CHALLENGE)
    public static class FulfillChallengeHandler implements CommandHandler {

        @Override
        public Response handle(Request request, Socket client) throws Exception {
            SignInEndPoint signInEndPoint = new SignInEndPoint();
            signInEndPoint.setSocket(client);

            return signInEndPoint.fulfillChallenge(request);
        }

        @Override
        public boolean shouldBeKeptAlive() {
            return false;
        }
    }
}
