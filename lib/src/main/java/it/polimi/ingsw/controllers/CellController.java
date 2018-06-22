package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.net.mocks.ICell;

import java.rmi.RemoteException;

public interface CellController extends ProxyUpdateInterface<ICell> {

    ICell getCell() throws RemoteException;

    Die tryToPick() throws RemoteException, DieInteractionException;

    void tryToPut(Die die) throws RemoteException, DieInteractionException;
}
