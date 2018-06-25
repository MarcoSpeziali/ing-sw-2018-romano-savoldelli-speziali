package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.net.mocks.IDie;

import java.rmi.RemoteException;

public interface DieController extends ProxyUpdateInterface<IDie> {
    IDie getDie() throws RemoteException;

    void setShade(Integer shade) throws RemoteException;
}