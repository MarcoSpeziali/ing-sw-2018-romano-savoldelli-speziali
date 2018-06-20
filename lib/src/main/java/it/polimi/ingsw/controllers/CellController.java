package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Die;

import java.rmi.RemoteException;

public interface CellController extends ProxyUpdateInterface<Cell> {
    Cell getCell() throws RemoteException;

    Die tryToPick() throws RemoteException, DieInteractionException;

    void tryToPut(Die die) throws RemoteException, DieInteractionException;
}
