package it.polimi.ingsw.server.net.sockets.middlewares;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.requests.SignUpRequest;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.net.endpoints.SignUpEndPoint;
import it.polimi.ingsw.server.net.sockets.ClientHandler;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

@Handles(EndPointFunction.SIGN_UP)
public class SignUpMiddleware implements Middleware {

    private SignUpEndPoint signUpEndPoint;

    @Override
    public void prepare() throws Exception {
        signUpEndPoint = new SignUpEndPoint();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Response handleRequest(Request<? extends JSONSerializable> request, EndPointFunction endPointFunction, ClientHandler clientHandler) {

        if (endPointFunction == EndPointFunction.SIGN_UP && request.getBody() instanceof SignUpRequest) {
            return signUpEndPoint.requestSignUp((Request<SignUpRequest>) request);
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
