package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.Window;

import java.rmi.RemoteException;

public interface WindowController extends ProxyUpdateInterface<Window> {
    Window getWindow() throws RemoteException;

    Die tryToPick(Die die) throws RemoteException, DieInteractionException;

    Die tryToPick(Integer integer) throws RemoteException, DieInteractionException;

    void tryToPut(Die die, Integer location) throws RemoteException, DieInteractionException;
}
