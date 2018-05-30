package it.polimi.ingsw.net;

import java.util.function.Consumer;

public interface LobbyLookupInterface {
    void requestLookup(String token, Consumer<LobbyInfo> updateFunction, Consumer<Math> onStart);
}
