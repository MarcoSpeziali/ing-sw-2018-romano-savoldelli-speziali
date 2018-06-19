package it.polimi.ingsw.net.interfaces.updates;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UpdateInterface<T extends Serializable> extends Remote, Serializable {
    void onUpdateReceived(T update) throws RemoteException;
}
