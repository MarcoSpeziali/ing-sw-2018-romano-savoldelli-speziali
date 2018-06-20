package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.models.Die;

import java.rmi.RemoteException;

public interface DieController extends ProxyUpdateInterface<Die> {
    Die getDie() throws RemoteException;

    void setShade(Integer shade) throws RemoteException;
}