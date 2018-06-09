package it.polimi.ingsw.server.events;

import it.polimi.ingsw.server.sql.DatabasePlayer;

@Emits(EventType.PLAYER_EVENTS)
public interface PlayerEventsListener extends IEvent {
    default void onPlayerConnected(DatabasePlayer player) {}
    default void onPlayerDisconnected(DatabasePlayer player) {}
}
