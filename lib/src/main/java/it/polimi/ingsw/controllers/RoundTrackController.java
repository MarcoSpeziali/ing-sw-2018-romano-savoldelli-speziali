package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.RoundTrack;

import java.rmi.RemoteException;

public interface RoundTrackController extends ProxyUpdateInterface<RoundTrack> {
    RoundTrack getRoundTrack() throws RemoteException;

    Die tryToPick(Die die) throws RemoteException, DieInteractionException;

    Die tryToPick(Integer location) throws RemoteException, DieInteractionException;

    void tryToPut(Die die, Integer location) throws RemoteException, DieInteractionException;
}
