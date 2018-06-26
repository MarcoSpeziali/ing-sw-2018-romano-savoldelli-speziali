package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.models.Bag;
import it.polimi.ingsw.models.ObjectiveCard;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.server.controllers.DraftPoolControllerImpl;
import it.polimi.ingsw.server.controllers.RoundTrackControllerImpl;
import it.polimi.ingsw.server.controllers.ToolCardControllerImpl;
import it.polimi.ingsw.server.controllers.WindowControllerImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// TODO: docs
public class MatchObjectsManager {

    private static final Map<Integer, MatchObjectsManager> instances = new HashMap<>();

    public static MatchObjectsManager getManagerForMatch(IMatch match) {
        return getManagerForMatch(match.getId());
    }

    public static MatchObjectsManager getManagerForMatch(int matchId) {
        return instances.computeIfAbsent(matchId, MatchObjectsManager::new);
    }

    private final int matchId;

    private Bag bag;
    private DraftPoolControllerImpl draftPoolController;
    private RoundTrackControllerImpl roundTrackController;
    private ObjectiveCard[] publicObjectiveCards;
    private ToolCardControllerImpl[] toolCardControllers;

    private Map<IPlayer, WindowControllerImpl> playerWindowMap;
    private Map<IPlayer, ObjectiveCard> playerPrivateObjectiveCardMap;

    private MatchObjectsManager(int matchId) {
        this.matchId = matchId;

        playerWindowMap = new HashMap<>();
        playerPrivateObjectiveCardMap = new HashMap<>();
    }

    public Bag getBag() {
        return bag;
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }

    public DraftPoolControllerImpl getDraftPoolController() {
        return draftPoolController;
    }

    public void setDraftPoolController(DraftPoolControllerImpl draftPoolController) {
        this.draftPoolController = draftPoolController;
    }

    public RoundTrackControllerImpl getRoundTrackController() {
        return roundTrackController;
    }

    public void setRoundTrackController(RoundTrackControllerImpl roundTrackController) {
        this.roundTrackController = roundTrackController;
    }

    public ObjectiveCard[] getPublicObjectiveCards() {
        return publicObjectiveCards;
    }

    public void setPublicObjectiveCards(ObjectiveCard[] publicObjectiveCards) {
        this.publicObjectiveCards = publicObjectiveCards;
    }

    public ToolCardControllerImpl[] getToolCardControllers() {
        return toolCardControllers;
    }

    public void setToolCardControllers(ToolCardControllerImpl[] toolCardControllers) {
        this.toolCardControllers = toolCardControllers;
    }

    public WindowControllerImpl getWindowControllerForPlayer(IPlayer player) {
        return playerWindowMap.get(player);
    }
    
    public Map<IPlayer, WindowControllerImpl> getPlayerWindowMap() {
        return playerWindowMap;
    }
    
    public void setWindowControllerForPlayer(WindowControllerImpl windowController, IPlayer player) {
        this.playerWindowMap.put(player, windowController);
    }

    public ObjectiveCard getObjectiveCardForPlayer(IPlayer player) {
        return playerPrivateObjectiveCardMap.get(player);
    }

    public void setPrivateObjectiveCardForPlayer(ObjectiveCard objectiveCard, IPlayer player) {
        this.playerPrivateObjectiveCardMap.put(player, objectiveCard);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MatchObjectsManager that = (MatchObjectsManager) o;
        return matchId == that.matchId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchId);
    }
}
