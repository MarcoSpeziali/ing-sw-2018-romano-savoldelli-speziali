package it.polimi.ingsw.client.net;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.Settings;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.LobbyInterface;
import it.polimi.ingsw.net.interfaces.updates.UpdateInterface;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.providers.PersistentNetworkInteractionProvider;
import it.polimi.ingsw.net.providers.PersistentRMIInteractionProvider;
import it.polimi.ingsw.net.providers.PersistentSocketInteractionProvider;
import it.polimi.ingsw.net.requests.LobbyJoinRequest;
import it.polimi.ingsw.net.utils.EndPointFunction;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class LobbyManager {
    private static LobbyManager instance = new LobbyManager();

    private PersistentNetworkInteractionProvider persistentNetworkInteractionProvider;

    private LobbyManager() {
        if (Settings.getSettings().getProtocol().equals(Constants.Protocols.SOCKETS)) {
            persistentNetworkInteractionProvider = new PersistentSocketInteractionProvider(
                    Settings.getSettings().getServerSocketAddress(),
                    Settings.getSettings().getServerSocketPort()
            );
        }
        else {
            persistentNetworkInteractionProvider = new PersistentRMIInteractionProvider<>(
                    Settings.getSettings().getServerRMIAddress(),
                    Settings.getSettings().getServerRMIPort(),
                    LobbyInterface.class
            );
        }
    }

    public static LobbyManager getManager() {
        return instance;
    }

    public boolean joinLobby(UpdateInterface<ILobby> lobbyUpdateInterface) throws IOException, NotBoundException, ReflectiveOperationException {
        Constants.Protocols protocol = Settings.getSettings().getProtocol();

        EndPointFunction endPointFunction = protocol == Constants.Protocols.SOCKETS ? EndPointFunction.LOBBY_JOIN_REQUEST :
                EndPointFunction.LOBBY_JOIN_REQUEST_RMI;

        persistentNetworkInteractionProvider.open(endPointFunction);

        // builds the join request
        Request<LobbyJoinRequest> joinRequest = new Request<>(
                new Header(SignInManager.getManager().getToken(), endPointFunction),
                new LobbyJoinRequest()
        );

        Response<ILobby> requestResponse;

        if (protocol == Constants.Protocols.SOCKETS) {
            PersistentSocketInteractionProvider persistentSocketInteractionProvider = (PersistentSocketInteractionProvider) this.persistentNetworkInteractionProvider;

            // makes the request
            requestResponse = persistentSocketInteractionProvider.getSyncResponseFor(joinRequest);
            persistentSocketInteractionProvider.listenFor(EndPointFunction.LOBBY_UPDATE_RESPONSE, response -> {
                try {
                    lobbyUpdateInterface.onUpdateReceived((ILobby) response.getBody());
                }
                catch (RemoteException ignored) {
                    // this exception cannot happen because updatesInterface is a local object
                }
            });
        }
        else {
            PersistentRMIInteractionProvider persistentRMIInteractionProvider = (PersistentRMIInteractionProvider) this.persistentNetworkInteractionProvider;

            // makes the request
            requestResponse = persistentRMIInteractionProvider.getSyncResponseFor(joinRequest, lobbyUpdateInterface);
        }

        if (requestResponse.getError() == null) {
            lobbyUpdateInterface.onUpdateReceived(requestResponse.getBody());
            return true;
        }

        return false;
    }
}
