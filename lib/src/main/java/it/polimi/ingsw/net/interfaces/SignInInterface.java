package it.polimi.ingsw.net.interfaces;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;

import java.rmi.Remote;
import java.rmi.RemoteException;

// TODO: docs
public interface SignInInterface extends Remote {
    Response requestLogin(Request request) throws RemoteException;
    Response fulfillChallenge(Request request) throws RemoteException;
}