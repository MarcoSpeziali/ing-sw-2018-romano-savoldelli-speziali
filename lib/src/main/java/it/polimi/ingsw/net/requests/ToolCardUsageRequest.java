package it.polimi.ingsw.net.requests;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.net.mocks.IToolCard;
import it.polimi.ingsw.net.mocks.ToolCardMock;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class ToolCardUsageRequest implements MatchInteraction {
    
    private static final long serialVersionUID = -2347171569269621331L;
    
    private final int matchId;
    private final IToolCard toolCard;
    
    public ToolCardUsageRequest(
            @JSONElement("match-id") int matchId,
            @JSONElement("tool-card") ToolCardMock toolCard
    ) {
        this.matchId = matchId;
        this.toolCard = toolCard;
    }
    
    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return matchId;
    }
    
    @JSONElement("tool-card")
    public IToolCard getToolCard() {
        return toolCard;
    }
}
