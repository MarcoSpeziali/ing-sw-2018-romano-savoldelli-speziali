package it.polimi.ingsw.server.events;

import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.net.mocks.IWindow;
import it.polimi.ingsw.server.managers.MatchCommunicationsManager;
import it.polimi.ingsw.server.sql.DatabasePlayer;

@Emits(EventType.MATCH_COMMUNICATION_EVENTS)
public interface MatchCommunicationsListener extends IEvent {
    default void onWindowChosen(MatchCommunicationsManager matchCommunicationsManager, DatabasePlayer databasePlayer, IWindow window) {
    }

    default void onPlayerTriedToMove(MatchCommunicationsManager matchCommunicationsManager, DatabasePlayer databasePlayer, Move move) {
    }
}
