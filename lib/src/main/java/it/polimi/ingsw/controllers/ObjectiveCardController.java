package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.models.ObjectiveCard;

import java.rmi.RemoteException;

public interface ObjectiveCardController extends ProxyUpdateInterface<ObjectiveCard> {
    ObjectiveCard getObjectiveCard() throws RemoteException;
}
