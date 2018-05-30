package it.polimi.ingsw.net.interfaces;

import it.polimi.ingsw.net.LobbyInfo;

import java.rmi.Remote;
import java.util.function.Consumer;

public interface LobbyLookupInterface extends Remote {
    void requestLookup(String token, Consumer<LobbyInfo> updateFunction, Consumer<Math> onStart);
}
