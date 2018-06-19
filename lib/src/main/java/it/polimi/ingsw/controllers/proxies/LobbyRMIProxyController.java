package it.polimi.ingsw.controllers.proxies;

import it.polimi.ingsw.controllers.LobbyController;
import it.polimi.ingsw.net.mocks.ILobby;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

@SuppressWarnings("squid:S2160")
public class LobbyRMIProxyController extends UnicastRemoteObject implements LobbyController, HeartBeatListener {

    private static final long serialVersionUID = 6402299999494663705L;
    private final transient Object syncObject = new Object();
    private ILobby lobbyResult;
    private Boolean shouldBeKeptAlive;

    public LobbyRMIProxyController() throws RemoteException {
        super();
        this.shouldBeKeptAlive = true;
    }

    @Override
    public void init() {
        // useless in rmi
    }

    @Override
    public ILobby waitForUpdate() throws InterruptedException {
        synchronized (syncObject) {
            while (getLobbyResult() == null) {
                // waits until there is a result
                syncObject.wait();
            }

            return resetLobbyResult();
        }
    }

    public ILobby getLobbyResult() {
        synchronized (syncObject) {
            return this.lobbyResult;
        }
    }

    public ILobby resetLobbyResult() {
        synchronized (syncObject) {
            ILobby result = this.lobbyResult;
            this.lobbyResult = null;
            return result;
        }
    }

    public void close() {
        shouldBeKeptAlive = false;
    }

    public void setStartingLobbyValue(ILobby startingLobbyValue) {
        this.lobbyResult = startingLobbyValue;
    }

    @Override
    public synchronized void onUpdateReceived(ILobby update) {
        synchronized (syncObject) {
            this.lobbyResult = update;

            syncObject.notifyAll();
        }
    }

    @Override
    public Boolean onHeartBeat() {
        return shouldBeKeptAlive;
    }
}
