package it.polimi.ingsw.server.net.handlers;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.utils.RequestFields;
import it.polimi.ingsw.server.net.CommandHandler;
import it.polimi.ingsw.server.net.endpoints.SignInEndPoint;

import java.net.Socket;

public class SignInHandlers {
    private SignInHandlers() {}

    public static class RequestLoginHandler implements CommandHandler {

        @Override
        public Response handle(Request request, Socket client) throws Exception {
            String clientAddress = client.getRemoteSocketAddress().toString();

            String[] tokens = clientAddress.split(":");

            return new SignInEndPoint(
                    request,
                    tokens[0],
                    Integer.parseInt(tokens[1])
            ).requestLogin(
                    (String) request.getRequestBody().get(RequestFields.Authentication.USERNAME.getFieldName())
            );
        }

        @Override
        public boolean shouldBeKeptAlive() {
            return false;
        }
    }

    public static class FulfillChallengeHandler implements CommandHandler {

        @Override
        public Response handle(Request request, Socket client) throws Exception {
            String clientAddress = client.getRemoteSocketAddress().toString();

            String[] tokens = clientAddress.split(":");

            return new SignInEndPoint(
                    request,
                    tokens[0],
                    Integer.parseInt(tokens[1])
            ).fulfillChallenge(
                    (int) request.getRequestBody().get(RequestFields.Authentication.SESSION_ID.getFieldName()),
                    (String) request.getRequestBody().get(RequestFields.Authentication.CHALLENGE_RESPONSE.getFieldName())
            );
        }

        @Override
        public boolean shouldBeKeptAlive() {
            return false;
        }
    }
}
