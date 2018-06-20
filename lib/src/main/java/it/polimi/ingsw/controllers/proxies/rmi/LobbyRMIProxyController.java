package it.polimi.ingsw.controllers.proxies.rmi;

import it.polimi.ingsw.controllers.LobbyController;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.mocks.IMatch;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

@SuppressWarnings("squid:S2160")
public class LobbyRMIProxyController extends UnicastRemoteObject implements LobbyController, HeartBeatListener {

    private static final long serialVersionUID = 6402299999494663705L;
    private final transient Object matchSyncObject = new Object();
    private final transient Object updateSyncObject = new Object();
    private ILobby lobbyResult;
    private IMatch matchResult;
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
        synchronized (updateSyncObject) {
            while (getLobbyResult() == null) {
                // waits until there is a result
                updateSyncObject.wait();
            }

            return resetLobbyResult();
        }
    }

    private ILobby getLobbyResult() {
        synchronized (updateSyncObject) {
            return this.lobbyResult;
        }
    }

    private ILobby resetLobbyResult() {
        synchronized (updateSyncObject) {
            ILobby result = this.lobbyResult;
            this.lobbyResult = null;
            return result;
        }
    }

    @Override
    public IMatch waitForMigrationRequest() throws InterruptedException {
        synchronized (matchSyncObject) {
            while (getMatchResult() == null) {
                matchSyncObject.wait();
            }

            return getMatchResult();
        }
    }

    private IMatch getMatchResult() {
        synchronized (matchSyncObject) {
            return this.matchResult;
        }
    }

    public void leave() {
        shouldBeKeptAlive = false;
    }

    public void setStartingLobbyValue(ILobby startingLobbyValue) {
        this.lobbyResult = startingLobbyValue;
    }

    @Override
    public synchronized void onUpdateReceived(ILobby update) {
        synchronized (updateSyncObject) {
            this.lobbyResult = update;

            updateSyncObject.notifyAll();
        }
    }

    @Override
    public Boolean onHeartBeat() {
        return shouldBeKeptAlive;
    }
}
