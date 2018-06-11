package it.polimi.ingsw.server.net.commands;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.requests.SignUpRequest;
import it.polimi.ingsw.net.responses.SignUpResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.net.endpoints.SignUpEndPoint;

import java.net.Socket;
import java.rmi.RemoteException;

@Handles(EndPointFunction.SIGN_UP)
public class SignUpCommand implements Command<SignUpResponse, SignUpRequest> {


    @Override
    public Response<SignUpResponse> handle(Request<SignUpRequest> request, Socket client) throws RemoteException {
        return new SignUpEndPoint().requestSignUp(request);
    }

    @Override
    public boolean shouldBeKeptAlive() {
        return false;
    }
}
