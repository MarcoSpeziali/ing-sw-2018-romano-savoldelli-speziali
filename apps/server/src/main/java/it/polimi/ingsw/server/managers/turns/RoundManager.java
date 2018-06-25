package it.polimi.ingsw.server.managers.turns;

import it.polimi.ingsw.net.mocks.ILivePlayer;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.server.events.EventDispatcher;
import it.polimi.ingsw.server.events.PlayerEventsListener;
import it.polimi.ingsw.server.sql.DatabasePlayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RoundManager implements PlayerEventsListener, AutoCloseable {
    
    private static final Map<Integer, RoundManager> managers = new HashMap<>();
    
    public static final byte NUMBER_OF_ROUNDS = 10;
    
    /**
     * Gets an instance of {@link RoundManager} which handles the specified {@link IMatch}.
     *
     * @param iMatch the {@link IMatch} to handle
     * @return an instance of {@link RoundManager} which handles the provided {@link IMatch}
     */
    public static RoundManager createRoundManager(IMatch iMatch) {
        RoundManager roundManager = new RoundManager(iMatch);
        managers.put(iMatch.getId(), roundManager);
        return roundManager;
    }
    
    /**
     * Gets instance of {@link RoundManager} which handles the specified {@link IMatch}
     * or {@code null} if a {@link RoundManager} for the provided {@link IMatch} has not been created.
     *
     * @param iMatch the {@link IMatch} to handle
     * @return the instance of {@link RoundManager} which handles the specified {@link IMatch}
     *         or {@code null} if a {@link RoundManager} for the provided {@link IMatch} has not been created.
     */
    public static RoundManager getRoundManager(IMatch iMatch) {
        return managers.getOrDefault(iMatch.getId(), null);
    }
    
    private PlayerTurnList playerTurnList;
    private List<IPlayer> originalPlayers;
    private Turn currentTurn;
    
    private RoundManager(IMatch iMatch) {
        this.originalPlayers = Arrays.stream(iMatch.getPlayers())
                .map(ILivePlayer::getPlayer)
                .collect(Collectors.toList());
    
        this.playerTurnList = new PlayerTurnList(
                this.originalPlayers.toArray(new IPlayer[0]),
                NUMBER_OF_ROUNDS
        );
    
        EventDispatcher.register(this);
    }
    
    /**
     * Gets the current {@link Turn} or {@code null} if no more turns are available.
     *
     * @return the current {@link Turn} or {@code null} if no more turns are available
     */
    public Turn current() {
        return this.currentTurn;
    }
    
    /**
     * Gets the next {@link Turn} or {@code null} if no more turns are available.
     *
     * @return the next {@link Turn} or {@code null} if no more turns are available
     */
    public Turn next() {
        if (this.hasNext()) {
            this.currentTurn = this.playerTurnList.next();
            
            return this.currentTurn;
        }
    
        return null;
    }
    
    /**
     * Gets if there is an other {@link Turn} available.
     *
     * @return if there is an other {@link Turn} available
     */
    public boolean hasNext() {
        return this.playerTurnList.hasNext();
    }
    
    @Override
    public void onPlayerConnected(DatabasePlayer player) {
        // if the connected player was in this match then it is re-added in the turn list
        if (this.originalPlayers.contains(player)) {
            this.playerTurnList.addPlayer(player);
        }
    }
    
    @Override
    public void onPlayerDisconnected(DatabasePlayer player) {
        // if the connected player was in this match then it is removed from the turn list
        if (this.originalPlayers.contains(player)) {
            this.playerTurnList.removePlayer(player);
        }
    }
    
    @Override
    public void close() {
        // avoids the receiving of any other events
        EventDispatcher.unregister(this);
    }
}
