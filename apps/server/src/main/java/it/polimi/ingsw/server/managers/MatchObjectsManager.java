package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.controllers.*;
import it.polimi.ingsw.models.Bag;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.server.controllers.WindowControllerImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private DraftPoolController draftPoolController;
    private RoundTrackController roundTrackController;
    private ObjectiveCardController[] publicObjectiveCardControllers;
    private ToolCardController[] toolCardControllers;

    private Map<IPlayer, WindowControllerImpl> playerWindowMap;
    private Map<IPlayer, ObjectiveCardController> playerObjectiveCardControllerMap;

    private MatchObjectsManager(int matchId) {
        this.matchId = matchId;

        playerWindowMap = new HashMap<>();
        playerObjectiveCardControllerMap = new HashMap<>();
    }

    public Bag getBag() {
        return bag;
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }

    public DraftPoolController getDraftPoolController() {
        return draftPoolController;
    }

    public void setDraftPoolController(DraftPoolController draftPoolController) {
        this.draftPoolController = draftPoolController;
    }

    public RoundTrackController getRoundTrackController() {
        return roundTrackController;
    }

    public void setRoundTrackController(RoundTrackController roundTrackController) {
        this.roundTrackController = roundTrackController;
    }

    public ObjectiveCardController[] getPublicObjectiveCardControllers() {
        return publicObjectiveCardControllers;
    }

    public void setPublicObjectiveCardControllers(ObjectiveCardController[] publicObjectiveCardControllers) {
        this.publicObjectiveCardControllers = publicObjectiveCardControllers;
    }

    public ToolCardController[] getToolCardControllers() {
        return toolCardControllers;
    }

    public void setToolCardControllers(ToolCardController[] toolCardControllers) {
        this.toolCardControllers = toolCardControllers;
    }

    public WindowControllerImpl getWindowControllerForPlayer(IPlayer player) {
        return playerWindowMap.get(player);
    }

    public void setWindowControllerForPlayer(WindowControllerImpl windowController, IPlayer player) {
        this.playerWindowMap.put(player, windowController);
    }

    public ObjectiveCardController getObjectiveCardControllerForPlayer(IPlayer player) {
        return playerObjectiveCardControllerMap.get(player);
    }

    public void setObjectiveCardControllerForPlayer(ObjectiveCardController objectiveCardController, IPlayer player) {
        this.playerObjectiveCardControllerMap.put(player, objectiveCardController);
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
