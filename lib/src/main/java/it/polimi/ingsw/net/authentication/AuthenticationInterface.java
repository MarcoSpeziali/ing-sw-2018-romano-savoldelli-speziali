package it.polimi.ingsw.net.authentication;

import it.polimi.ingsw.net.AuthenticationResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthenticationInterface extends Remote {
    AuthenticationResponse requestLogin(String username) throws RemoteException;
    AuthenticationResponse fulfillChallenge(String sessionId, String challengeResponse) throws RemoteException;
}