package it.polimi.ingsw.server.net.commands;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.requests.ChallengeRequest;
import it.polimi.ingsw.net.requests.SignInRequest;
import it.polimi.ingsw.net.responses.ChallengeResponse;
import it.polimi.ingsw.net.responses.SignInResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.net.endpoints.SignInEndPoint;

import java.net.Socket;
import java.rmi.RemoteException;

public class SignInCommands {
    private SignInCommands() {}

    @Handles(EndPointFunction.SIGN_IN_REQUEST_AUTHENTICATION)
    public static class SignInRequestCommand implements Command<ChallengeRequest, SignInRequest> {

        @Override
        public Response<ChallengeRequest> handle(Request<SignInRequest> request, Socket client) throws RemoteException {
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
    public static class FulfillChallengeCommand implements Command<SignInResponse, ChallengeResponse> {

        @Override
        public Response<SignInResponse> handle(Request<ChallengeResponse> request, Socket client) throws RemoteException {
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
