package it.polimi.ingsw.controllers.proxies;

import it.polimi.ingsw.net.interfaces.updates.UpdateInterface;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProxyUpdateInterface<T extends Serializable> extends Remote, Serializable, UpdateInterface<T> {

    @Override
    default void onUpdateReceived(T update) throws RemoteException {
        // do nothing by default
    }

    T waitForUpdate() throws RemoteException, InterruptedException;
}
