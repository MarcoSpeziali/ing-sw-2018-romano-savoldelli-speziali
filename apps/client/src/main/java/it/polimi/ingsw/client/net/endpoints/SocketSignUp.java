package it.polimi.ingsw.client.net.endpoints;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.SignUpInterface;

import java.rmi.RemoteException;

public class SocketSignUp implements SignUpInterface {
    @Override
    public Response requestSignUp(Request request) throws RemoteException {
        return null;
    }
}
