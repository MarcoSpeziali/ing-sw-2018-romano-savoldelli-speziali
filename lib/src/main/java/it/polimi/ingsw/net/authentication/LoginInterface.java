package it.polimi.ingsw.net.authentication;

import it.polimi.ingsw.net.Response;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LoginInterface extends Remote {
    Response requestLogin(String username) throws RemoteException;
    Response fulfillChallenge(String sessionId, String challengeResponse) throws RemoteException;
}