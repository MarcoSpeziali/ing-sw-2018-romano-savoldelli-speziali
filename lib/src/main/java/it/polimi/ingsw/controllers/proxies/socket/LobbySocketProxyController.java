package it.polimi.ingsw.controllers.proxies.socket;

import it.polimi.ingsw.controllers.LobbyController;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.providers.PersistentSocketInteractionProvider;
import it.polimi.ingsw.net.requests.LobbyJoinRequest;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper;

import java.io.IOException;
import java.rmi.RemoteException;

public class LobbySocketProxyController implements LobbyController {

    private static final long serialVersionUID = -6798215473610760987L;

    private final String clientToken;

    private final transient Object matchSyncObject = new Object();
    private final transient Object updateSyncObject = new Object();

    private transient PersistentSocketInteractionProvider persistentSocketInteractionProvider;
    private transient ILobby lobbyResult;
    private transient IMatch matchResult;

    public LobbySocketProxyController(String remoteHost, int remotePort, String clientToken) {
        this.clientToken = clientToken;

        this.persistentSocketInteractionProvider = new PersistentSocketInteractionProvider(
                remoteHost,
                remotePort
        );
    }

    @Override
    @SuppressWarnings("squid:S1602")
    public void init(Object... args) throws IOException {
        this.persistentSocketInteractionProvider.open(EndPointFunction.LOBBY_JOIN_REQUEST);

        try {
            persistentSocketInteractionProvider.getAsyncResponseFor(new Request<>(
                    new Header(
                            clientToken,
                            EndPointFunction.LOBBY_JOIN_REQUEST
                    ),
                    new LobbyJoinRequest()
            ), (Response<ILobby> response) -> {
                if (response.getError() != null) {
                    FunctionalExceptionWrapper.wrap(new RemoteException());
                }

                synchronized (updateSyncObject) {
                    this.lobbyResult = response.getBody();
                    updateSyncObject.notifyAll();
                }
            }, FunctionalExceptionWrapper::wrap);
        }
        catch (FunctionalExceptionWrapper e) {
            e.tryUnwrap(RemoteException.class)
                    .tryFinalUnwrap(IOException.class);
        }

        persistentSocketInteractionProvider.listenFor(
                EndPointFunction.LOBBY_UPDATE_RESPONSE,
                response -> {
                    synchronized (updateSyncObject) {
                        this.lobbyResult = (ILobby) response.getBody();
                        updateSyncObject.notifyAll();
                    }
                });

        persistentSocketInteractionProvider.listenFor(
                EndPointFunction.MATCH_MIGRATION,
                response -> {
                    synchronized (matchSyncObject) {
                        this.matchResult = (IMatch) response.getBody();
                        matchSyncObject.notifyAll();
                    }
                }
        );
    }

    @Override
    public ILobby waitForUpdate() throws InterruptedException {
        synchronized (updateSyncObject) {
            while (getLobbyResult() == null) {
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

    public void close(Object... args) throws IOException {
        this.persistentSocketInteractionProvider.close();
    }
}
