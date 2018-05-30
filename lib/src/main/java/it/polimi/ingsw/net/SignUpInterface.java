package it.polimi.ingsw.net;

import java.rmi.RemoteException;

public interface SignUpInterface {
    Response requestSignUp(String username, String encryptedPassword) throws RemoteException;
}
