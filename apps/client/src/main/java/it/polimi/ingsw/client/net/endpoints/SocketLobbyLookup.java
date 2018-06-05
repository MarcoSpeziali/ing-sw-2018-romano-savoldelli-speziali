package it.polimi.ingsw.client.net.endpoints;

import it.polimi.ingsw.net.LobbyInfo;
import it.polimi.ingsw.net.interfaces.LobbyLookupInterface;

import java.util.function.Consumer;

public class SocketLobbyLookup implements LobbyLookupInterface {

    @Override
    public void requestLookup(String token, Consumer<LobbyInfo> updateFunction, Consumer<Math> onStart) {

    }
}
