package it.polimi.ingsw.controllers.proxies.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HeartBeatListener extends Remote {
    /**
     * @return false if the client wants to close the connection
     */
    Boolean onHeartBeat() throws RemoteException;
}
