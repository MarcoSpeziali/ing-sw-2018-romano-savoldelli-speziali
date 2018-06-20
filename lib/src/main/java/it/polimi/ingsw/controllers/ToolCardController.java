package it.polimi.ingsw.controllers;

import it.polimi.ingsw.models.ToolCard;
import it.polimi.ingsw.net.interfaces.updates.UpdateInterface;

import java.rmi.RemoteException;

public interface ToolCardController extends UpdateInterface<ToolCard> {
    ToolCard getToolCard() throws RemoteException;

    void requestUsage() throws RemoteException, NotEnoughTokensException;

    boolean canUse() throws RemoteException;
}
