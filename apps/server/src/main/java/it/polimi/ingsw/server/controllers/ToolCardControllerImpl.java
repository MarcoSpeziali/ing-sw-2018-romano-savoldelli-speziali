package it.polimi.ingsw.server.controllers;

import it.polimi.ingsw.controllers.NotEnoughTokensException;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.models.ToolCard;
import it.polimi.ingsw.net.mocks.ILivePlayer;
import it.polimi.ingsw.server.Effect;
import it.polimi.ingsw.server.actions.*;
import it.polimi.ingsw.server.managers.MatchObjectsManager;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.utils.GlobalContext;

public class ToolCardControllerImpl {
    
    private final ToolCard toolCard;
    private final int matchId;
    private final MatchObjectsManager matchObjectsManager;

    public ToolCardControllerImpl(ToolCard toolCard, int matchId, MatchObjectsManager matchObjectsManager) {
        this.toolCard = toolCard;
        this.matchId = matchId;
        this.matchObjectsManager = matchObjectsManager;
    }
    
    public ToolCard getToolCard() {
        return this.toolCard;
    }
    
    public void requestUsage(DatabasePlayer databasePlayer, int tokens) {
        if (!canUse(tokens)) {
            throw new NotEnoughTokensException(this.toolCard.getEffect().getCost(), tokens);
        }
        
        Effect effect = (Effect) this.toolCard.getEffect();
        try {
            effect.run(
                    toolCard.getCardId(),
                    GlobalContext.getGlobalContext().getContextForPlayer(databasePlayer, this.matchId)
            );
        }
        catch (UnCompletableActionException e) {
            this.matchObjectsManager.getDraftPoolController().getDraftPool().putDie(e.getDie());
        }
    }

    public boolean canUse(int tokensCount) {
        return this.toolCard.getEffect().getCost() <= tokensCount;
    }

    public boolean canUse(ILivePlayer livePlayer) {
        return canUse(livePlayer.getFavourTokens());
    }

    public void setUserInteractionProvider(UserInteractionProvider userInteractionProvider) {
        ((Effect) this.toolCard.getEffect()).getActions()
                .forEach(ea -> this.setUserInteractionProvider(userInteractionProvider, ea));
    }

    public void setActionGroupCallbacks(ActionGroupCallbacks actionGroupCallbacks) {
        ((Effect) this.toolCard.getEffect()).getActions().stream()
                .filter(ActionGroup.class::isInstance)
                .map(executableAction -> ((ActionGroup) executableAction))
                .forEach(actionGroup -> this.setActionGroupCallbacks(actionGroupCallbacks, actionGroup));
    }

    private void setActionGroupCallbacks(ActionGroupCallbacks actionGroupCallbacks, ActionGroup actionGroup) {
        actionGroup.setCallbacks(actionGroupCallbacks);
        actionGroup.getActions().stream()
                .filter(ActionGroup.class::isInstance)
                .map(executableAction -> ((ActionGroup) executableAction))
                .forEach(actionGroup1 -> this.setActionGroupCallbacks(actionGroupCallbacks, actionGroup1));
    }

    private void setUserInteractionProvider(UserInteractionProvider userInteractionProvider, ExecutableAction executableAction) {
        if (executableAction instanceof Action) {
            ((Action) executableAction).setUserInteractionProvider(userInteractionProvider);
        }
        else if (executableAction instanceof ActionGroup) {
            ((ActionGroup) executableAction).getActions().forEach(ea -> this.setUserInteractionProvider(userInteractionProvider, ea));
        }
    }
}
