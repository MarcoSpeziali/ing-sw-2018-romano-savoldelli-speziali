package it.polimi.ingsw.server.controllers;

import it.polimi.ingsw.controllers.NotEnoughTokensException;
import it.polimi.ingsw.models.ToolCard;
import it.polimi.ingsw.net.mocks.ILivePlayer;
import it.polimi.ingsw.server.Effect;
import it.polimi.ingsw.server.utils.GlobalContext;

public class ToolCardControllerImpl {
    
    private final ToolCard toolCard;
    private final int matchId;
    
    public ToolCardControllerImpl(ToolCard toolCard, int matchId) {
        this.toolCard = toolCard;
        this.matchId = matchId;
    }
    
    public synchronized ToolCard getToolCard() {
        return this.toolCard;
    }
    
    public synchronized void requestUsage(ILivePlayer livePlayer) throws NotEnoughTokensException {
        if (!canUse(livePlayer)) {
            throw new NotEnoughTokensException(this.toolCard.getEffect().getCost(), livePlayer.getFavourTokens());
        }
        
        Effect effect = (Effect) this.toolCard.getEffect();
        effect.run(
                toolCard.getCardId(),
                GlobalContext.getGlobalContext().getContextForPlayer(livePlayer.getPlayer(), matchId)
        );
    }
    
    public synchronized boolean canUse(ILivePlayer livePlayer) {
        return this.toolCard.getEffect().getCost() <= livePlayer.getFavourTokens();
    }
}
