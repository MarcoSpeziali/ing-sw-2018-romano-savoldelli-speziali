package it.polimi.ingsw.controllers;

import it.polimi.ingsw.models.ToolCard;
import it.polimi.ingsw.net.interfaces.updates.UpdateInterface;
import it.polimi.ingsw.net.mocks.IToolCard;

import java.rmi.RemoteException;

public interface ToolCardController extends UpdateInterface<IToolCard> {
    IToolCard getToolCard() throws RemoteException;

    void requestUsage() throws RemoteException, NotEnoughTokensException;

    boolean canUse() throws RemoteException;
}
