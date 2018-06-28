package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.net.mocks.IWindow;

import java.io.IOException;
import java.rmi.RemoteException;

public interface WindowController extends ProxyUpdateInterface<IWindow> {
    IWindow getWindow() throws RemoteException;

    CellController getCellController(int i, int j) throws RemoteException;

    IDie tryToPick(IDie die) throws IOException;

    IDie tryToPick(Integer location) throws RemoteException;

    void tryToPut(IDie die, Integer location) throws RemoteException, DieInteractionException;
}
