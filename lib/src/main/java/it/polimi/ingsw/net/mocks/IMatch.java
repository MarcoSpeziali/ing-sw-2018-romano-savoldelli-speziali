package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.core.Player;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public interface IMatch extends JSONSerializable {

    @JSONElement("id")
    int getId();

    @JSONElement("starting-time")
    long getStartingTime();

    @JSONElement("ending-time")
    long getEndingTime();

    @JSONElement("lobby")
    ILobby getLobby();

    @JSONElement("players")
    ILivePlayer[] getPlayers();
    
    @JSONElement("draft-pool")
    IDraftPool getDraftPool();
    
    @JSONElement("round-track")
    IRoundTrack getRoundTrack();
    
    @JSONElement("public-objective-cards")
    IObjectiveCard[] getPublicObjectiveCards();
    
    @JSONElement("tool-cards")
    IToolCard[] getToolCards();
    
    @JSONElement("private-objective-card")
    IObjectiveCard getPrivateObjectiveCard();

    @JSONElement("current-player")
    Player getCurrentPlayer();
}
