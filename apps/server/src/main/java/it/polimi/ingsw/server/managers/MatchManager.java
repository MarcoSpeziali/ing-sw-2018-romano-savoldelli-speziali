package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.controllers.proxies.rmi.MatchRMIProxyController;
import it.polimi.ingsw.controllers.proxies.socket.MatchSocketProxyController;
import it.polimi.ingsw.models.*;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.server.Settings;
import it.polimi.ingsw.server.controllers.*;
import it.polimi.ingsw.server.events.*;
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

public class MatchManager implements PlayerEventsListener, MatchCommunicationsListener {

    // ------ MATCH OBJECTS ------
    private DatabaseMatch databaseMatch;
    private final MatchCommunicationsManager matchCommunicationsManager;
    private final MatchObjectsManager matchObjectsManager;

    // ------ MATCH OBJECTS CACHE ------
    private final List<IPlayer> lobbyPlayers;
    private List<Window> possibleChosenWindows;
    private List<IPlayer> matchPlayers;
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
    
    public MatchMock getMatch() {
        return new MatchMock(
                this.databaseMatch,
                playerToLivePlayerMap.values()
                        .toArray(new ILivePlayer[0])
        );
    }
    
    public MatchManager(int lobbyId) throws SQLException {
        this.databaseMatch = DatabaseMatch.insertMatchFromLobby(lobbyId);
        this.lobbyPlayers = Arrays.asList(DatabaseLobby.getLobbyWithId(lobbyId).getPlayers());

        // creates the executor service
        this.matchExecutorService = Executors.newScheduledThreadPool(1);

        this.setUpConnectionTimer();

        this.matchCommunicationsManager = new MatchCommunicationsManager(this.databaseMatch);
        this.matchObjectsManager = MatchObjectsManager.getManagerForMatch(this.databaseMatch);
        this.matchPlayers = new ArrayList<>(this.lobbyPlayers.size());
        this.playerToLivePlayerMap = new HashMap<>();
    }
    
    // ------ LIFE CYCLE ------
    
    public void addPlayer(DatabasePlayer databasePlayer, MatchSocketProxyController matchSocketProxyController) throws SQLException {
        if (canAcceptPlayer(databasePlayer)) {
            this.matchCommunicationsManager.addPlayer(databasePlayer, matchSocketProxyController);

            this.addPlayerCommons(databasePlayer);
        }
    }
    
    public void addPlayer(DatabasePlayer databasePlayer, MatchRMIProxyController matchController) throws SQLException {
        if (canAcceptPlayer(databasePlayer)) {
            this.matchCommunicationsManager.addPlayer(databasePlayer, matchController);
    
            if (this.rmiHeartBeatScheduledFuture.isCancelled()) {
                this.setUpHeartBeat();
            }

            this.addPlayerCommons(databasePlayer);
        }
    }
    
    private boolean canAcceptPlayer(DatabasePlayer player) throws SQLException {
        return lobbyPlayers.contains(player) && this.matchState != MatchState.WAITING_FOR_PLAYERS || this.databaseMatch.getLeftPlayers().contains(player);
    }
    
    public void addPlayerCommons(DatabasePlayer databasePlayer) throws SQLException {
        this.databaseMatch = DatabaseMatch.insertPlayer(this.databaseMatch.getId(), databasePlayer.getId());
        this.matchPlayers.add(databasePlayer);
    
        IPlayer[] players = Arrays.stream(this.databaseMatch.getPlayers())
                .map(ILivePlayer::getPlayer)
                .toArray(IPlayer[]::new);

        // if the number of players in the match is equal to the number of players in the lobby
        // then the connection timer is stopped and the windows can be sent
        if (players.length == this.lobbyPlayers.size()) {
            this.connectionTimerScheduledFuture.cancel(true);
            
            try {
                // the pre-processing is done by connectionTask, so it can be run manually instead of waiting for the
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
    
    public void sendWindowsToChoose(IPlayer[] players) throws IOException, ClassNotFoundException {
        this.matchCommunicationsManager.sendWindowsToChoose(createWindows(players));
    }
    
    // ------ MODELS AND CONTROLLERS CREATION ------
    
    private Map<IPlayer, IWindow[]> createWindows(IPlayer[] players) throws IOException, ClassNotFoundException {
        Map<IPlayer, IWindow[]> playerWindowsMap = new HashMap<>();

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
    
        List<IPlayer> playersInLobby = Arrays.asList(lobby.getPlayers());
        List<IPlayer> playersInMatch = Arrays.stream(this.databaseMatch.getPlayers())
                .map(ILivePlayer::getPlayer)
                .collect(Collectors.toList());
    
        List<IPlayer> timedOutPlayers = new ArrayList<>(playersInLobby);
        timedOutPlayers.removeAll(playersInMatch);
    
        try {
            timedOutPlayers.forEach(wrap(iPlayer -> {
                DatabaseLobby.removePlayer(lobby.getId(), iPlayer.getId());
            }));
    
            this.sendWindowsToChoose(playersInMatch.toArray(new IPlayer[0]));
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
    
    // ------ EXTERNAL EVENTS ------
    
    @Override
    public void onPlayerDisconnected(DatabasePlayer player) {
    
    }

    @Override
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
        matchCommunicationsManager.sendWindowController(windowController);
        
        if (this.matchObjectsManager.getPlayerWindowMap().size() == this.matchPlayers.size()) {
            this.matchState = MatchState.WAITING_FOR_CONTROLLERS_ACK;
    
            try {
                createRemainingModelsAndControllers();
                // TODO: send models
            }
            catch (IOException | ClassNotFoundException e) {
                ServerLogger.getLogger().log(Level.SEVERE, "Error occurred while creating models and controllers:", e);
                throw new RuntimeException(e);
            }
        }
    }

    private enum MatchState {
        WAITING_FOR_PLAYERS,
        WAITING_FOR_WINDOW_RESPONSES,
        WAITING_FOR_CONTROLLERS_ACK,
        RUNNING_ROUNDS,
        CALCULATING_RESULTS,
        CLOSING_MATCH
    }
}
