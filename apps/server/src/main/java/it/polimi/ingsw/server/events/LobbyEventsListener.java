package it.polimi.ingsw.server.events;

import it.polimi.ingsw.server.sql.DatabaseLobby;
import it.polimi.ingsw.server.sql.DatabasePlayer;

@Emits(EventType.LOBBY_EVENTS)
public interface LobbyEventsListener extends IEvent {
    default void onPlayerJoined(DatabaseLobby lobby, DatabasePlayer player) {
    }

    default void onPlayerLeft(DatabaseLobby lobby, DatabasePlayer player) {
    }

    default void onLobbyOpened(DatabaseLobby lobby) {
    }

    default void onLobbyClosed(DatabaseLobby lobby, LobbyCloseReason reason) {
    }

    enum LobbyCloseReason {
        MATCH_STARTED,
        ALL_PLAYERS_LEFT
    }
}
