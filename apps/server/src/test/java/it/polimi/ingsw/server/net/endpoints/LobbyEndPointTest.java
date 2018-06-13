package it.polimi.ingsw.server.net.endpoints;

import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.requests.LobbyJoinRequest;
import org.junit.Test;

import java.rmi.RemoteException;

class LobbyEndPointTest {
    
    @Test
    void testLobbyEndPoint() throws RemoteException {
        LobbyEndPoint lobbyEndPoint = new LobbyEndPoint();
        lobbyEndPoint.joinLobby(new Request<>(
                new Header("token"),
                new LobbyJoinRequest()
        ));
    }
}