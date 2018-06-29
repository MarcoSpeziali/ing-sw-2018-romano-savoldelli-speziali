package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.controllers.DieInteractionException;
import it.polimi.ingsw.controllers.proxies.rmi.MatchRMIProxyController;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.models.*;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.server.Settings;
import it.polimi.ingsw.server.controllers.*;
import it.polimi.ingsw.server.events.*;
import it.polimi.ingsw.server.managers.turns.RoundManager;
import it.polimi.ingsw.server.managers.turns.Turn;
import it.polimi.ingsw.server.net.sockets.AuthenticatedClientHandler;
import it.polimi.ingsw.server.sql.DatabaseLobby;
import it.polimi.ingsw.server.sql.DatabaseMatch;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.utils.ArrayUtils;
import it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper;
import it.polimi.ingsw.utils.streams.StreamUtils;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;
import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.wrap;

public class MatchManager implements PlayerEventsListener, MatchCommunicationsListener, ModelUpdateListener {

    private static Map<Integer, MatchManager> matchManagerMap = new HashMap<>();

    public static MatchManager getManagerForMatch(int matchId) {
        try {
            final int lobbyId = DatabaseMatch.matchWithId(matchId).getLobby().getId();

            return matchManagerMap.computeIfAbsent(matchId, wrap(id -> {
                return new MatchManager(lobbyId);
            }));
        }
        catch (SQLException | NullPointerException | FunctionalExceptionWrapper e) {
            return null;
        }
    }

    // ------ MATCH OBJECTS ------
    private DatabaseMatch databaseMatch;
    private MatchCommunicationsManager matchCommunicationsManager;
    private final MatchObjectsManager matchObjectsManager;
    private RoundManager roundManager;

    // ------ MATCH OBJECTS CACHE ------
    private final List<DatabasePlayer> lobbyPlayers;
    private List<Window> possibleChosenWindows;
    private List<DatabasePlayer> matchPlayers;
    private Map<IPlayer, ILivePlayer> playerToLivePlayerMap;

    // ------ TIMERS AND FUTURES ------
    private final ScheduledExecutorService matchExecutorService;
    /**
     * The {@link ScheduledFuture} that sends the rmi heartbeat.
     */
    private ScheduledFuture<?> rmiHeartBeatScheduledFuture;
    /**
     * The {@link ScheduledFuture} that handles the connection timer.
     */
    private ScheduledFuture<?> connectionTimerScheduledFuture;
    /**
     * The {@link ScheduledFuture} that handles the move timer.
     */
    private ScheduledFuture<?> moveTimerScheduledFuture;

    // ------ FSM OBJECTS ------
    private MatchState matchState = MatchState.WAITING_FOR_PLAYERS;
    
    // ------ TURN SETTINGS ------
    private boolean shouldIgnoreColor = false;
    private boolean shouldIgnoreShade = false;
    private boolean shouldIgnoreAdjacency = false;
    
    public boolean shouldIgnoreColor() {
        return shouldIgnoreColor;
    }
    
    public void setShouldIgnoreColor(boolean shouldIgnoreColor) {
        this.shouldIgnoreColor = shouldIgnoreColor;
    }
    
    public boolean shouldIgnoreShade() {
        return shouldIgnoreShade;
    }
    
    public void setShouldIgnoreShade(boolean shouldIgnoreShade) {
        this.shouldIgnoreShade = shouldIgnoreShade;
    }
    
    public boolean shouldIgnoreAdjacency() {
        return shouldIgnoreAdjacency;
    }
    
    public void setShouldIgnoreAdjacency(boolean shouldIgnoreAdjacency) {
        this.shouldIgnoreAdjacency = shouldIgnoreAdjacency;
    }

    public DatabaseMatch getDatabaseMatch() {
        return this.databaseMatch;
    }
    
    public MatchManager(int lobbyId) throws SQLException {
        this.databaseMatch = DatabaseMatch.insertMatchFromLobby(lobbyId);
        this.lobbyPlayers = Arrays.stream(DatabaseLobby.getLobbyWithId(lobbyId).getPlayers())
                .map(DatabasePlayer.class::cast)
                .collect(Collectors.toList());

        // creates the executor service
        this.matchExecutorService = Executors.newScheduledThreadPool(1);

        this.setUpConnectionTimer();

        this.matchCommunicationsManager = new MatchCommunicationsManager(
                this.databaseMatch.toMatchMock(),
                this
        );
        this.matchObjectsManager = MatchObjectsManager.getManagerForMatch(this.databaseMatch);
        this.matchPlayers = new ArrayList<>(this.lobbyPlayers.size());
        this.playerToLivePlayerMap = new HashMap<>();

        matchManagerMap.putIfAbsent(this.databaseMatch.getId(), this);
    }
    
    // ------ LIFE CYCLE ------
    
    public void addPlayer(DatabasePlayer databasePlayer, AuthenticatedClientHandler authenticatedClientHandler) throws SQLException {
        if (canAcceptPlayer(databasePlayer)) {
            this.matchCommunicationsManager.addPlayer(databasePlayer, authenticatedClientHandler);

            this.addPlayerCommons(databasePlayer);
        }
    }
    
    public void addPlayer(DatabasePlayer databasePlayer, MatchRMIProxyController matchController) throws SQLException {
        if (canAcceptPlayer(databasePlayer)) {
            this.matchCommunicationsManager.addPlayer(databasePlayer, matchController);
    
            if (this.rmiHeartBeatScheduledFuture == null || this.rmiHeartBeatScheduledFuture.isCancelled()) {
                this.setUpHeartBeat();
            }

            this.addPlayerCommons(databasePlayer);
        }
    }
    
    private boolean canAcceptPlayer(DatabasePlayer player) throws SQLException {
        return lobbyPlayers.contains(player) && this.matchState == MatchState.WAITING_FOR_PLAYERS || this.databaseMatch.getLeftPlayers().contains(player);
    }

    private void addPlayerCommons(DatabasePlayer databasePlayer) throws SQLException {
        DatabaseMatch.insertPlayer(this.databaseMatch.getId(), databasePlayer.getId());
        this.matchPlayers.add(databasePlayer);
    
        DatabasePlayer[] players = this.databaseMatch.getDatabasePlayers();

        // if the number of players in the match is equal to the number of players in the lobby
        // then the connection timer is stopped and the windows can be sent
        if (players.length == this.lobbyPlayers.size()) {
            this.connectionTimerScheduledFuture.cancel(true);
            
            try {
                // the pre-processing is done by connectionTask, so it can be run
                // manually instead of waiting for the timer to expire
                connectionTask.run();
            }
            catch (Exception e) {
                ServerLogger.getLogger().log(Level.SEVERE, "Error occurred in the connection task:", e);
                throw new RuntimeException(e);
            }
        }

        EventDispatcher.dispatch(
                EventType.MATCH_EVENTS,
                MatchEventListeners.class,
                matchEventListeners -> matchEventListeners.onPlayerMigrated(this.databaseMatch, databasePlayer)
        );
    }
    
    // ------ NETWORK COMMUNICATION ------

    private void sendWindowsToChoose(DatabasePlayer[] players) throws IOException, ClassNotFoundException {
        this.matchCommunicationsManager.sendWindowsToChoose(createWindows(players));
    }
    
    // ------ MODELS AND CONTROLLERS CREATION ------
    
    private Map<DatabasePlayer, IWindow[]> createWindows(DatabasePlayer[] players) throws IOException, ClassNotFoundException {
        Map<DatabasePlayer, IWindow[]> playerWindowsMap = new HashMap<>();

        Window[] windows = InstantiationManager.instantiateWindows();
        windows = ArrayUtils.shuffleArray(windows);

        int windowsPerPlayer = Settings.getSettings().getNumberOfWindowsPerPlayerToChoose();

        this.possibleChosenWindows = new ArrayList<>(players.length * windowsPerPlayer);

        for (int i = 0; i < players.length; i++) {
            Window[] possibleWindows =  ArrayUtils.sliceArray(windows, windowsPerPlayer * i, windowsPerPlayer * (i + 1));
            this.possibleChosenWindows.addAll(Arrays.asList(possibleWindows));

            playerWindowsMap.put(players[i], possibleWindows);

            playerWindowsMap.computeIfPresent(
                    players[i],
                    (iPlayer, iWindows) -> Arrays.stream(iWindows)
                            .map(WindowMock::new)
                            .toArray(IWindow[]::new)
            );
        }

        return playerWindowsMap;
    }
    
    private void createRemainingModelsAndControllers() throws IOException, ClassNotFoundException {
        // ------ DO NOT NEED A CONTROLLER ------
        Bag bag = new Bag(Settings.getSettings().getNumberOfDicePerColorInBag());
        ObjectiveCard[] publicObjectiveCards = getPublicObjectiveCards();
        ObjectiveCard[] privateObjectiveCards = getPrivateObjectiveCards();
        
        this.matchObjectsManager.setBag(bag);
        this.matchObjectsManager.setPublicObjectiveCards(publicObjectiveCards);
        StreamUtils.indexedForEach(
                this.matchPlayers,
                (i, player) -> this.matchObjectsManager.setPrivateObjectiveCardForPlayer(
                        privateObjectiveCards[i],
                        player
                )
        );
    
        // ------ NEEDS A CONTROLLER ------
        RoundTrack roundTrack = new RoundTrack();
        DraftPool draftPool = new DraftPool((byte) this.matchPlayers.size());
        ToolCard[] toolCards = getToolCards();
    
        RoundTrackControllerImpl roundTrackController = new RoundTrackControllerImpl(roundTrack);
        DraftPoolControllerImpl draftPoolController = new DraftPoolControllerImpl(draftPool);
        ToolCardControllerImpl[] toolCardControllers = Arrays.stream(toolCards)
                .map(ToolCardControllerImpl::new)
                .toArray(ToolCardControllerImpl[]::new);
        
        this.matchObjectsManager.setRoundTrackController(roundTrackController);
        this.matchObjectsManager.setDraftPoolController(draftPoolController);
        this.matchObjectsManager.setToolCardControllers(toolCardControllers);
    }
    
    private ObjectiveCard[] getPublicObjectiveCards() throws IOException, ClassNotFoundException {
        ObjectiveCard[] objectiveCards = InstantiationManager.instantiatePublicObjectiveCards();
        objectiveCards = ArrayUtils.shuffleArray(objectiveCards);
        
        return ArrayUtils.sliceArray(
                objectiveCards,
                Settings.getSettings().getNumberOfPublicObjectiveCards()
        );
    }
    
    private ObjectiveCard[] getPrivateObjectiveCards() throws IOException, ClassNotFoundException {
        ObjectiveCard[] objectiveCards = InstantiationManager.instantiatePrivateObjectiveCards();
        objectiveCards = ArrayUtils.shuffleArray(objectiveCards);
        
        return ArrayUtils.sliceArray(
                objectiveCards,
                Settings.getSettings().getNumberOfPrivateObjectiveCards() * this.matchPlayers.size()
        );
    }
    
    private ToolCard[] getToolCards() throws IOException, ClassNotFoundException {
        ToolCard[] toolCards = InstantiationManager.instantiateToolCards();
        toolCards = ArrayUtils.shuffleArray(toolCards);
    
        return ArrayUtils.sliceArray(
                toolCards,
                Settings.getSettings().getNumberOfToolCards()
        );
    }
    
    // ------ TIMERS AND TASKS ------
    
    @SuppressWarnings("squid:S1602") // Lambdas containing only one statement should not nest this statement in a block
    private final Runnable connectionTask = unsafe(() -> {
        // on expiration match starts:
        // the players that did not respond to migration request
        // are marked as left from the lobby, they cannot join back
        this.matchState = MatchState.WAITING_FOR_WINDOW_RESPONSES;
    
        ILobby lobby = this.databaseMatch.getLobby();
    
        List<DatabasePlayer> playersInLobby = Arrays.stream(lobby.getPlayers())
                .map(DatabasePlayer.class::cast)
                .collect(Collectors.toList());
        
        List<DatabasePlayer> playersInMatch = Arrays.asList(this.databaseMatch.getDatabasePlayers());
    
        List<DatabasePlayer> timedOutPlayers = new ArrayList<>(playersInLobby);
        timedOutPlayers.removeAll(playersInMatch);
    
        try {
            timedOutPlayers.forEach(wrap(iPlayer -> {
                DatabaseLobby.removePlayer(lobby.getId(), iPlayer.getId());
            }));

            this.matchCommunicationsManager.closeConnectionToOtherPlayers();
    
            this.sendWindowsToChoose(playersInMatch.toArray(new DatabasePlayer[0]));

            this.matchState = MatchState.RUNNING_ROUNDS;
        }
        catch (FunctionalExceptionWrapper e) {
            e.tryUnwrap(SQLException.class)
                    .tryUnwrap(IOException.class)
                    .tryFinalUnwrap(ClassNotFoundException.class);
        }
    });
    
    private final Runnable moveTimerExecutorTask = () -> {
        // on expiration turn is over and the next player is awaken
    };
    
    /**
     * Sets up the connection timer.
     */
    private void setUpConnectionTimer() {
        this.connectionTimerScheduledFuture = matchExecutorService.scheduleWithFixedDelay(
                connectionTask,
                Settings.getSettings().getMatchConnectionTimerDuration(),
                Long.MAX_VALUE, // prevents the execution (gives time to cancel the timer)
                Settings.getSettings().getMatchConnectionTimerTimeUnit()
        );
    }

    /**
     * Sets up the heart beat timer.
     */
    private void setUpHeartBeat() {
        this.rmiHeartBeatScheduledFuture = this.matchExecutorService.scheduleAtFixedRate(() -> {
                    List<DatabasePlayer> scheduledForDeletion = new LinkedList<>();
            
                    //noinspection Duplicates
                    this.matchCommunicationsManager.forEachRmi((player, matchRMIProxyController) -> {
                        try {
                            if (!matchRMIProxyController.onHeartBeat()) {
                                throw new RemoteException();
                            }
                        }
                        catch (RemoteException e) {
                            scheduledForDeletion.add(player);
                        }
                    });
                    
                    scheduledForDeletion.forEach(player -> {
                        this.matchCommunicationsManager.removePlayer(player);
                        EventDispatcher.dispatch(EventType.PLAYER_EVENTS, PlayerEventsListener.class, playerEventsListener -> playerEventsListener.onPlayerDisconnected(player));
                    });
                },
                0,
                Settings.getSettings().getRmiHeartBeatMatchTimeout(),
                Settings.getSettings().getRmiHeartBeatMatchTimeUnit()
        );
    }

    // ------ MOVE HANDLING ------

    private void handleMove(Move move, DatabasePlayer databasePlayer, Turn currentTurn) throws IOException {
        /*
            started         = 0b00
            die_placed      = 0b01
            tool_card_used  = 0b10
            ended           = 0b11

            to proceed the phase must be or started or tool_card_used
            so:
                0b00
                0b10
                = 0b0-

            since we are interested only in the first be the phase gets masked with 0b10
            */
        if ((currentTurn.getPhase() & 0b10) == 0b00) {
            try {
                IDie requested = this.matchObjectsManager.getDraftPoolController()
                        .getDieAtLocation(move.getDraftPoolPickPosition());

                if (requested == null) {
                    throw new DieInteractionException();
                }

                WindowControllerImpl windowController = this.matchObjectsManager.getWindowControllerForPlayer(databasePlayer);

                if (!windowController.canPutDieAtLocation(requested, move.getWindowPutPosition())) {
                    throw new DieInteractionException();
                }

                Die die = this.matchObjectsManager.getDraftPoolController()
                        .tryToPick(move.getDraftPoolPickPosition());
                windowController.tryToPut(
                        die,
                        move.getWindowPutPosition(),
                        this.shouldIgnoreColor,
                        this.shouldIgnoreShade,
                        this.shouldIgnoreAdjacency
                );

                this.matchCommunicationsManager.sendPositiveResponseForMoveToPlayer(databasePlayer);
            }
            catch (DieInteractionException e) {
                this.matchCommunicationsManager.sendNegativeResponseForMoveToPlayer(databasePlayer);
            }
        }
        else {
            this.matchCommunicationsManager.sendNegativeResponseForMoveToPlayer(databasePlayer);
        }
    }

    // ------ PLAYER EVENTS ------
    
    @Override
    public void onPlayerDisconnected(DatabasePlayer player) {

    }

    // ------ MODELS EVENTS ------

    @Override
    public void onModelUpdated(Object sender, Object model) {
        if (!this.matchObjectsManager.containsController(sender)) {
            return;
        }

        try {
            for (DatabasePlayer player : this.matchPlayers) {
                IMatch match = this.matchObjectsManager.buildMatchMockForPlayer(player);
                this.matchCommunicationsManager.sendMatchMockToPlayer(player, match);
            }
        }
        catch (IOException | SQLException e) {
            ServerLogger.getLogger().log(Level.SEVERE, "Error occurred while creating models and controllers:", e);
            throw new RuntimeException(e);
        }
    }

    // ------ COMMUNICATIONS EVENTS ------

    @Override
    @SuppressWarnings("squid:S00112")
    public void onWindowChosen(MatchCommunicationsManager matchCommunicationsManager, DatabasePlayer databasePlayer, IWindow window) {
        if (matchCommunicationsManager != this.matchCommunicationsManager) {
            return;
        }

        Window chosenWindow = this.possibleChosenWindows.stream()
                .filter(w -> w.equals(window))
                .findFirst()
                .orElseThrow(RuntimeException::new);

        WindowControllerImpl windowController = new WindowControllerImpl(chosenWindow, CellControllerImpl::new);

        this.matchObjectsManager.setWindowControllerForPlayer(windowController, databasePlayer);

        if (this.matchObjectsManager.getPlayerWindowMap().size() == this.matchPlayers.size()) {
            this.matchState = MatchState.RUNNING_ROUNDS;

            try {
                createRemainingModelsAndControllers();

                for (DatabasePlayer player : this.matchPlayers) {
                    IMatch match = this.matchObjectsManager.buildMatchMockForPlayer(player);
                    this.matchCommunicationsManager.sendMatchMockToPlayer(player, match);
                }

                this.roundManager = RoundManager.createRoundManager(this.databaseMatch);
            }
            catch (IOException | ClassNotFoundException | SQLException e) {
                ServerLogger.getLogger().log(Level.SEVERE, "Error occurred while creating models and controllers and sending mocks:", e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onPlayerTriedToMove(MatchCommunicationsManager matchCommunicationsManager, DatabasePlayer databasePlayer, Move move) {
        if (matchCommunicationsManager != this.matchCommunicationsManager) {
            return;
        }

        Turn currentTurn = this.roundManager.current();

        try {
            this.handleMove(move, databasePlayer, currentTurn);
            currentTurn.appendPhase(Turn.DIE_PLACED);

            if (currentTurn.getPhase() == Turn.ENDED) {
                currentTurn.end();
                this.roundManager.next();
            }
        }
        catch (IOException e) {
            ServerLogger.getLogger().log(Level.SEVERE, "Error while sending move response to user: " + databasePlayer.toString(), e);
        }
    }

    private enum MatchState {
        WAITING_FOR_PLAYERS,
        WAITING_FOR_WINDOW_RESPONSES,
        RUNNING_ROUNDS,
        CALCULATING_RESULTS,
        CLOSING_MATCH
    }
}
