package it.polimi.ingsw.server.net.sockets.middlewares;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.requests.SignInRequest;
import it.polimi.ingsw.net.responses.ChallengeResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.net.endpoints.SignInEndPoint;
import it.polimi.ingsw.server.net.sockets.ClientHandler;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.rmi.RemoteException;

@Handles({EndPointFunction.SIGN_IN_REQUEST_AUTHENTICATION, EndPointFunction.SIGN_IN_FULFILL_CHALLENGE})
public class SignInMiddleware implements Middleware {

    private SignInEndPoint signInEndPoint;

    @Override
    public void prepare() throws RemoteException {
        signInEndPoint = new SignInEndPoint();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Response handleRequest(Request<? extends JSONSerializable> request, EndPointFunction endPointFunction, ClientHandler clientHandler) {
        if (endPointFunction == EndPointFunction.SIGN_IN_REQUEST_AUTHENTICATION && request.getBody() instanceof SignInRequest) {
            signInEndPoint.setSocket(clientHandler.getClient());
            return signInEndPoint.requestSignIn((Request<SignInRequest>) request);
        }
        else if (endPointFunction == EndPointFunction.SIGN_IN_FULFILL_CHALLENGE && request.getBody() instanceof ChallengeResponse) {
            signInEndPoint.setSocket(clientHandler.getClient());
            return signInEndPoint.fulfillChallenge((Request<ChallengeResponse>) request);
        }

        return null;
    }

    @Override
    public Request handleResponse(Response<? extends JSONSerializable> response, EndPointFunction endPointFunction, ClientHandler clientHandler) {
        return null;
    }

    @Override
    public boolean shouldBeKeptAlive() {
        return false;
    }
}
