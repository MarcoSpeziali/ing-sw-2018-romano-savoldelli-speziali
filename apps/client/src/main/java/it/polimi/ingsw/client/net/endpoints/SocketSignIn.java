package it.polimi.ingsw.client.net.endpoints;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.SignInInterface;

import java.rmi.RemoteException;

public class SocketSignIn implements SignInInterface {
    @Override
    public Response requestLogin(Request request) throws RemoteException {
        return null;
    }

    @Override
    public Response fulfillChallenge(Request request) throws RemoteException {
        return null;
    }
}
