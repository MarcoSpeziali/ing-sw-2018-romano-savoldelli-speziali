package it.polimi.ingsw.net.interfaces;

import it.polimi.ingsw.net.Response;

import java.rmi.Remote;
import java.rmi.RemoteException;

// TODO: docs
public interface SignInInterface extends Remote {
    Response requestLogin(String username) throws RemoteException;
    Response fulfillChallenge(int sessionId, String challengeResponse) throws RemoteException;
}