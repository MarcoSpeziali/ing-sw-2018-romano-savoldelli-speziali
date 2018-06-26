package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.net.mocks.ICell;
import it.polimi.ingsw.net.mocks.IDie;

import java.rmi.RemoteException;

public interface CellController extends ProxyUpdateInterface<ICell> {

    ICell getCell() throws RemoteException;

    IDie tryToPick() throws RemoteException;

    void tryToPut(IDie die) throws RemoteException, DieInteractionException;
}
