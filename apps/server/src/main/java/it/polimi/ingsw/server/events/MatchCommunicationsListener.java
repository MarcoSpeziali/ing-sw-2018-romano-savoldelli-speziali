package it.polimi.ingsw.server.events;

import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.net.mocks.IToolCard;
import it.polimi.ingsw.net.mocks.IWindow;
import it.polimi.ingsw.net.responses.MoveResponse;
import it.polimi.ingsw.server.managers.MatchCommunicationsManager;
import it.polimi.ingsw.server.sql.DatabasePlayer;

public interface MatchCommunicationsListener extends IEvent {
    void onWindowChosen(MatchCommunicationsManager matchCommunicationsManager, DatabasePlayer databasePlayer, IWindow window);

    MoveResponse onPlayerTriedToMove(MatchCommunicationsManager matchCommunicationsManager, DatabasePlayer databasePlayer, Move move);
    
    void onPlayerEndRequest(MatchCommunicationsManager matchCommunicationsManager, DatabasePlayer databasePlayer);
    
    void onPlayerRequestedToolCard(MatchCommunicationsManager matchCommunicationsManager, DatabasePlayer databasePlayer, IToolCard toolCard);
}
