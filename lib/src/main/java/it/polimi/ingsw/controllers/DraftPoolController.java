package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.DraftPool;

import java.rmi.RemoteException;

public interface DraftPoolController extends ProxyUpdateInterface<DraftPool> {
    DraftPool getDraftPool() throws RemoteException;

    Die tryToPick(Die die) throws RemoteException, DieInteractionException;

    Die tryToPick(Integer location) throws RemoteException, DieInteractionException;

    void tryToPut(Die die) throws RemoteException, DieInteractionException;
}
