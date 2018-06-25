package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.models.ObjectiveCard;
import it.polimi.ingsw.net.mocks.IObjectiveCard;

import java.rmi.RemoteException;

public interface ObjectiveCardController extends ProxyUpdateInterface<IObjectiveCard> {
    IObjectiveCard getObjectiveCard() throws RemoteException;
}
