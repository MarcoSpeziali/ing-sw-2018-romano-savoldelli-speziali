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

import java.util.*;
import java.util.stream.Collectors;

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

    private Map<DatabasePlayer, WindowControllerImpl> playerWindowMap;
    private Map<DatabasePlayer, ObjectiveCard> playerPrivateObjectiveCardMap;

    private Map<DatabasePlayer, Integer> playerToTokensMap;
    private Set<DatabasePlayer> playersLeft;

    private MatchObjectsManager(DatabaseMatch match) {
        this.match = match;

        playerWindowMap = new HashMap<>();
        playerPrivateObjectiveCardMap = new HashMap<>();
        playersLeft = new HashSet<>(match.getLeftPlayers());
    }

    public void setPlayersLeft(Set<DatabasePlayer> playersLeft) {
        this.playersLeft = playersLeft;
    }

    private void createPlayers() {
        DatabasePlayer[] currentPlayers = this.match.getDatabasePlayers();
        List<DatabasePlayer> leftPlayers = this.match.getLeftPlayers();

        List<DatabasePlayer> allPlayers = new ArrayList<>(currentPlayers.length + leftPlayers.size());
        allPlayers.addAll(leftPlayers);
        allPlayers.addAll(List.of(currentPlayers));

        playerToTokensMap = allPlayers.stream()
                .map(player -> Map.entry(
                        player,
                        this.playerWindowMap.get(player).getWindow().getDifficulty())
                ).collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
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

    public WindowControllerImpl getWindowControllerForPlayer(DatabasePlayer player) {
        return playerWindowMap.get(player);
    }
    
    public Map<DatabasePlayer, WindowControllerImpl> getPlayerWindowMap() {
        return playerWindowMap;
    }
    
    public void setWindowControllerForPlayer(WindowControllerImpl windowController, DatabasePlayer player) {
        this.playerWindowMap.put(player, windowController);
    }

    public ObjectiveCard getObjectiveCardForPlayer(DatabasePlayer player) {
        return playerPrivateObjectiveCardMap.get(player);
    }

    public void setPrivateObjectiveCardForPlayer(ObjectiveCard objectiveCard, DatabasePlayer player) {
        this.playerPrivateObjectiveCardMap.put(player, objectiveCard);
    }
    
    public Map<DatabasePlayer, ObjectiveCard> getPrivateObjectiveCards() {
        return playerPrivateObjectiveCardMap;
    }

    public Map<DatabasePlayer, Integer> getFavourTokensMap() {
        return playerToTokensMap;
    }

    public IMatch buildMatchMockForPlayer(DatabasePlayer player) {
        if (this.playerToTokensMap == null) {
            this.createPlayers();
        }

        return new MatchMock(
                this.match.getId(),
                this.match.getStartingTime(),
                this.match.getEndingTime(),
                new LobbyMock(this.match.getLobby()),
                this.playerToTokensMap.entrySet().stream()
                        .filter(entry -> entry.getKey().getId() != player.getId())
                        .map(entry -> new LivePlayerMock(
                                entry.getValue(),
                                new WindowMock(this.playerWindowMap.get(entry.getKey()).getWindow()),
                                new PlayerMock(entry.getKey()),
                                this.playersLeft.contains(entry.getKey())
                        )).toArray(LivePlayerMock[]::new),
                new DraftPoolMock(this.draftPoolController.getDraftPool()),
                new RoundTrackMock(this.roundTrackController.getRoundTrack()),
                Arrays.stream(this.publicObjectiveCards)
                        .map(ObjectiveCardMock::new)
                        .toArray(ObjectiveCardMock[]::new),
                Arrays.stream(this.toolCardControllers)
                        .map(ToolCardControllerImpl::getToolCard)
                        .map(ToolCardMock::new)
                        .toArray(ToolCardMock[]::new),
                new ObjectiveCardMock(this.playerPrivateObjectiveCardMap.get(player)),
                new LivePlayerMock(
                        this.playerToTokensMap.get(player),
                        new WindowMock(this.playerWindowMap.get(player).getWindow()),
                        new PlayerMock(player),
                        false
                )
        );
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
