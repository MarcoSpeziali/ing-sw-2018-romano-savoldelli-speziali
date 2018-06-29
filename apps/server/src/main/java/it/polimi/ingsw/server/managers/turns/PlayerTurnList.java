package it.polimi.ingsw.server.managers.turns;

import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.utils.collections.BiHashMap;
import it.polimi.ingsw.utils.ArrayUtils;

import java.util.*;

// TODO: test
public class PlayerTurnList {
    
    private final LinkedList<PlayerTurn> turns = new LinkedList<>();
    private static final int NUMBER_OF_TURNS_PER_PLAYER = 2;
    private final int numberOfRounds;
    private final HashSet<IPlayer> removedPlayers;
    private final BiHashMap<IPlayer, Integer, HashSet<Integer>> roundsToSkipForPlayer = new BiHashMap<>();
    
    private int currentRound;
    private int currentTurn;
    
    /**
     * Gets the current round.
     *
     * @return the current round
     */
    public int getCurrentRound() {
        return currentRound;
    }
    
    /**
     * Gets the current turn.
     *
     * @return the current turn
     */
    public int getCurrentTurn() {
        return currentTurn;
    }
    
    /**
     * Instantiates a new {@link PlayerTurnList} and build the list of turns.
     *
     * @param players the array of players in the match
     * @param numberOfRounds the number of rounds of the match
     */
    @SuppressWarnings("squid:CommentedOutCodeLine")
    public PlayerTurnList(IPlayer[] players, int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
        
        // shuffling the array to avoid having any non-random order in the turns
        final IPlayer[] shuffledArray = ArrayUtils.shuffleArray(players);
        
        // the turn list holds NUMBER_OF_TURNS_PER_PLAYER * players.length items, the order is
        // shuffledArray -> inverted(shuffledArray) -> shuffledArray -> inverted(shuffledArray) -> ...
        for (int i = 0; i < NUMBER_OF_TURNS_PER_PLAYER; i++) {
            // if the round index is even the order is dictated by the shuffledArray
            // otherwise the order is the inverse of that array
            boolean isEvenRound = i % 2 == 0;
            
            // starts from 0 if even (shuffledArray.length - 1) otherwise
            // continues while (j < shuffledArray.length) if even (j > 0) otherwise
            // after each loop j++ if even j-- otherwise
            for (int j = isEvenRound ? 0 : (shuffledArray.length - 1);
                 (isEvenRound && j < shuffledArray.length) || (!isEvenRound && j > 0);
                 j += isEvenRound ? 1 : -1) {
                this.turns.add(new PlayerTurn(shuffledArray[j], i));
            }
        }
        
        this.removedPlayers = new HashSet<>(players.length);
    }
    
    /**
     * Prevents the {@code player} from playing turns.
     *
     * @param player the player to remove
     */
    public void removePlayer(IPlayer player) {
        this.removedPlayers.add(player);
        
        this.turns.stream()
                .filter(playerTurn -> playerTurn.getPlayer().equals(player))
                .forEach(playerTurn -> playerTurn.setShouldSkip(true));
    }
    
    /**
     * Lets the {@code player} playing turns, starting from the next available turn
     *
     * @param player the player to re-add
     */
    public void addPlayer(IPlayer player) {
        this.removedPlayers.remove(player);
    
        this.turns.stream()
                .filter(playerTurn -> playerTurn.player.equals(player) && playerTurn.turn >= currentTurn)
                .forEach(playerTurn -> playerTurn.setShouldSkip(false));
    }
    
    /**
     * Prevents the {@code player} from playing the provided turn.
     *
     * @param player the player which should skip the {@code turn}
     * @param turn the turn to skip
     */
    public void skipPlayerTurn(IPlayer player, final int turn) {
        this.turns.stream()
                // the turn
                .filter(playerTurn -> playerTurn.getPlayer().equals(player) &&
                        playerTurn.getTurn() == turn)
                // is set to unplayable
                .forEach(playerTurn -> playerTurn.setShouldSkip(true));
    }
    
    /**
     * Prevents the {@code player} from playing the provided turn in the provided round.
     *
     * @param player the player which should skip the {@code turn} in the {@code round}
     * @param turn the turn to skip in the {@code round}
     * @param round the round to skip
     */
    public void skipPlayerTurn(IPlayer player, final int turn, final int round) {
        // if the round is future the player gets registered to skip the turn in the round
        if (round > currentRound) {
            // if the pair key(player, round) is absent a new HashSet is created and the turn is added
            this.roundsToSkipForPlayer.computeIfAbsent(player, round, (p, r) -> new HashSet<>(Set.of(turn)));
            // if the pair key(player, round) is present the turn is added
            this.roundsToSkipForPlayer.computeIfPresent(player, round, (p, r, hashSet) -> {
                hashSet.add(turn);
                return hashSet;
            });
        }
        // if the round is the current round
        else if (round == currentRound) {
            this.turns.stream()
                    // the turn
                    .filter(playerTurn -> playerTurn.getPlayer().equals(player) &&
                            playerTurn.getTurn() == turn)
                    // is set to unplayable
                    .forEach(playerTurn -> playerTurn.setShouldSkip(true));
        }
    }
    
    /**
     * Gets whether there is at least a player that must play in the current round.
     *
     * @return whether there is at least a player that must play in the current round
     */
    public boolean hasNextForCurrentRound() {
        return this.currentTurn != this.turns.size() - 1;
    }
    
    /**
     * Gets whether there is at least a player that must play in the current round.
     *
     * @return whether there is at least a player that must play in the current round
     */
    public boolean hasNext() {
        return this.currentTurn != this.turns.size() - 1 && this.currentRound != this.numberOfRounds - 1;
    }
    
    /**
     * Gets the turn for the current player.
     *
     * @return the turn for the current player
     */
    public int getTurnForCurrentPlayer() {
        PlayerTurn playerTurn = this.getCurrentPlayerTurn();
        return playerTurn == null ? -1 : playerTurn.turn;
    }
    
    /**
     * Gets the next {@link Turn}.
     *
     * @return the next {@link Turn} or {@code null} if no more turns are available
     */
    public Turn next() {
        PlayerTurn playerTurn = nextTurn();
        return playerTurn == null ? null : new Turn(playerTurn.player, currentRound, playerTurn.turn);
    }
    
    /**
     * Gets the {@link IPlayer} that should play the next round.
     *
     * @return the {@link IPlayer} that should play the next round or {@code null} if no more turns are available
     */
    private PlayerTurn nextTurn() {
        // if no more turns are available null is returned
        if (hasNext()) {
            return null;
        }
        
        // if no more turns are available in the current round: the turn is reset and the round is incremented
        if (this.currentTurn == this.turns.size() - 1) {
            this.currentTurn = 0;
            this.currentRound++;
            
            // the turn list is reset
            resetTurns();
        }
    
        // gets the next player
        PlayerTurn playerTurn = this.turns.get(this.currentTurn);
        
        // if the player cannot play
        while (playerTurn.shouldSkip()) {
            // the next one is retrieved
            playerTurn = nextTurn();
            
            // if no more players are available null is propagated
            if (playerTurn == null) {
                return null;
            }
        }
        
        // finally returns the player that shall play
        return playerTurn;
    }
    
    private PlayerTurn getCurrentPlayerTurn() {
        return this.currentTurn < this.turns.size() ? this.turns.get(this.currentTurn) : null;
    }
    
    /**
     * Resets the turns list by resetting the turn playability.
     */
    private void resetTurns() {
        this.turns.forEach(playerTurn -> playerTurn.setShouldSkip(
                // the turn must be skipped if the player has been removed or
                // it has been registered to skip the turn in this round
                hasBeenRemoved(playerTurn.player) || scheduledToSkip(playerTurn.player, playerTurn.turn)
        ));
    }
    
    /**
     * @param player the player to test
     * @return if the player has been removed
     */
    private boolean hasBeenRemoved(IPlayer player) {
        return this.removedPlayers.contains(player);
    }
    
    /**
     * @param player the player to test
     * @param turn the turn to test
     * @return if the should skip the turn for this round
     */
    private boolean scheduledToSkip(IPlayer player, int turn) {
        HashSet<Integer> turnToSkip = this.roundsToSkipForPlayer.getOrDefault(player, currentRound, null);
        return turnToSkip != null && turnToSkip.contains(turn);
    }
    
    private static class PlayerTurn {
        
        private final IPlayer player;
        private final int turn;
        private boolean shouldSkip;
        
        private PlayerTurn(IPlayer player, int turn) {
            this.player = player;
            this.turn = turn;
        }
    
        public IPlayer getPlayer() {
            return player;
        }
    
        public int getTurn() {
            return turn;
        }
    
        public boolean shouldSkip() {
            return shouldSkip;
        }
    
        public void setShouldSkip(boolean shouldSkip) {
            this.shouldSkip = shouldSkip;
        }
    }
}
