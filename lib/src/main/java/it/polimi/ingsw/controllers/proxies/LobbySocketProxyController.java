package it.polimi.ingsw.controllers.proxies;

import it.polimi.ingsw.controllers.LobbyController;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.providers.PersistentSocketInteractionProvider;
import it.polimi.ingsw.net.requests.LobbyJoinRequest;
import it.polimi.ingsw.net.utils.EndPointFunction;

import java.io.IOException;
import java.rmi.RemoteException;

public class LobbySocketProxyController implements LobbyController {

    private static final long serialVersionUID = -6798215473610760987L;
    private final String clientToken;
    private final transient Object syncObject = new Object();
    private transient PersistentSocketInteractionProvider persistentSocketInteractionProvider;
    private transient ILobby lobbyResult;

    public LobbySocketProxyController(String remoteHost, int remotePort, String clientToken) {
        this.clientToken = clientToken;

        this.persistentSocketInteractionProvider = new PersistentSocketInteractionProvider(
                remoteHost,
                remotePort
        );
    }

    @Override
    @SuppressWarnings("squid:S1602")
    public void init() throws IOException {
        this.persistentSocketInteractionProvider.open(EndPointFunction.LOBBY_JOIN_REQUEST);

        // TODO async
        Response<ILobby> lobbyResponse = persistentSocketInteractionProvider.getSyncResponseFor(new Request<>(
                new Header(
                        clientToken,
                        EndPointFunction.LOBBY_JOIN_REQUEST
                ),
                new LobbyJoinRequest()
        ));

        if (lobbyResponse.getError() != null) {
            throw new RemoteException();
        }

        this.lobbyResult = lobbyResponse.getBody();

        persistentSocketInteractionProvider.listenFor(
                EndPointFunction.LOBBY_UPDATE_RESPONSE,
                response -> {
                    synchronized (syncObject) {
                        this.lobbyResult = (ILobby) response.getBody();
                        syncObject.notifyAll();
                    }
                });
    }

    @Override
    public ILobby waitForUpdate() throws InterruptedException {
        synchronized (syncObject) {
            while (getLobbyResult() == null) {
                syncObject.wait();
            }
        }

        return resetLobbyResult();
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

    public void close() throws IOException {
        this.persistentSocketInteractionProvider.close();
    }
}
