package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.net.mocks.IWindow;

import java.rmi.RemoteException;

public interface WindowController extends ProxyUpdateInterface<IWindow> {
    IWindow getWindow() throws RemoteException;

    CellController getCellController(int i, int j);

    Die tryToPick(IDie die) throws RemoteException, DieInteractionException;

    Die tryToPick(Integer integer) throws RemoteException, DieInteractionException;

    void tryToPut(IDie die, Integer location) throws RemoteException, DieInteractionException;
}
