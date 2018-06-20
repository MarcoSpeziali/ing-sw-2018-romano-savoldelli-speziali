package it.polimi.ingsw.server.events;

import it.polimi.ingsw.server.sql.DatabaseMatch;
import it.polimi.ingsw.server.sql.DatabasePlayer;

@Emits(EventType.MATCH_EVENTS)
public interface MatchEventListeners extends IEvent {
    default void onMatchCreated(DatabaseMatch databaseMatch) {
    }

    default void onMatchStarted(DatabaseMatch databaseMatch) {
    }

    default void onMatchEnded(DatabaseMatch databaseMatch) {
    }

    default void onPlayerLeft(DatabaseMatch databaseMatch, DatabasePlayer databasePlayer) {
    }

    default void onPlayerRejoined(DatabaseMatch databaseMatch, DatabasePlayer databasePlayer) {
    }
}
