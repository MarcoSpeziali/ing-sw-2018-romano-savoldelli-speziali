package it.polimi.ingsw.net.interfaces;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;

import java.rmi.RemoteException;

public interface SignUpInterface {
    Response requestSignUp(Request request) throws RemoteException;
}
