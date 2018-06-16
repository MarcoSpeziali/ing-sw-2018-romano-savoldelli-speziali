package it.polimi.ingsw.client.controllers;

import it.polimi.ingsw.client.net.LobbyManager;
import it.polimi.ingsw.net.interfaces.updates.UpdateInterface;
import it.polimi.ingsw.net.mocks.ILobby;

import java.io.IOException;
import java.rmi.NotBoundException;

public abstract class LobbyController implements UpdateInterface<ILobby> {

    public void requestLobby() {
        try {
            LobbyManager.getManager().joinLobby(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (NotBoundException e) {
            e.printStackTrace();
        }
        catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
}
