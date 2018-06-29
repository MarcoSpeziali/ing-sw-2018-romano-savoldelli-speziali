package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.models.Bag;
import it.polimi.ingsw.models.ObjectiveCard;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.server.controllers.DraftPoolControllerImpl;
import it.polimi.ingsw.server.controllers.RoundTrackControllerImpl;
import it.polimi.ingsw.server.controllers.ToolCardControllerImpl;
import it.polimi.ingsw.server.controllers.WindowControllerImpl;
import it.polimi.ingsw.server.sql.DatabaseMatch;
import it.polimi.ingsw.server.sql.DatabasePlayer;

import java.sql.SQLException;
import java.util.*;

// TODO: docs
public class MatchObjectsManager {

    private static final Map<DatabaseMatch, MatchObjectsManager> instances = new HashMap<>();

    public static MatchObjectsManager getManagerForMatch(DatabaseMatch match) {
        return instances.computeIfAbsent(match, MatchObjectsManager::new);
    }

    private final DatabaseMatch match;

    private Bag bag;
    private DraftPoolControllerImpl draftPoolController;
    private RoundTrackControllerImpl roundTrackController;
    private ObjectiveCard[] publicObjectiveCards;
    private ToolCardControllerImpl[] toolCardControllers;

    private Map<IPlayer, WindowControllerImpl> playerWindowMap;
    private Map<IPlayer, ObjectiveCard> playerPrivateObjectiveCardMap;

    private MatchObjectsManager(DatabaseMatch match) {
        this.match = match;

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

    public IMatch buildMatchMockForPlayer(DatabasePlayer player) throws SQLException {
        return new MatchMock(
                this.match.getId(),
                this.match.getStartingTime(),
                this.match.getEndingTime(),
                new LobbyMock(this.match.getLobby()),
                getLivePlayers(),
                this.draftPoolController.getDraftPool(),
                this.roundTrackController.getRoundTrack(),
                this.publicObjectiveCards,
                Arrays.stream(this.toolCardControllers)
                        .map(ToolCardControllerImpl::getToolCard)
                        .map(ToolCardMock::new)
                        .toArray(ToolCardMock[]::new),
                this.playerPrivateObjectiveCardMap.get(player)
        );
    }
    
    private LivePlayerMock[] getLivePlayers() throws SQLException {
        IPlayer[] currentPlayers = this.match.getPlayers();
        List<IPlayer> leftPlayers = this.match.getLeftPlayers();
        
        List<IPlayer> allPlayers = new ArrayList<>(currentPlayers.length + leftPlayers.size());
        allPlayers.addAll(leftPlayers);
        allPlayers.addAll(List.of(currentPlayers));
        
        return allPlayers.stream()
                .map(player -> new LivePlayerMock(
                        0,
                        new WindowMock(playerWindowMap.get(player).getWindow()),
                        new PlayerMock(player),
                        leftPlayers.contains(player)
                ))
                .toArray(LivePlayerMock[]::new);
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
        return match.getId() == that.match.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(match.getId());
    }
}
