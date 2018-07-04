package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.controllers.DieInteractionException;
import it.polimi.ingsw.controllers.NotEnoughTokensException;
import it.polimi.ingsw.controllers.proxies.rmi.MatchRMIProxyController;
import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.models.*;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.net.responses.MoveResponse;
import it.polimi.ingsw.server.Objective;
import it.polimi.ingsw.server.Settings;
import it.polimi.ingsw.server.controllers.*;
import it.polimi.ingsw.server.events.*;
import it.polimi.ingsw.server.managers.turns.RoundManager;
import it.polimi.ingsw.server.managers.turns.Turn;
import it.polimi.ingsw.server.net.sockets.AuthenticatedClientHandler;
import it.polimi.ingsw.server.sql.DatabaseLobby;
import it.polimi.ingsw.server.sql.DatabaseMatch;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.utils.GlobalContext;
import it.polimi.ingsw.server.utils.MatchContextHelper;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.utils.ArrayUtils;
import it.polimi.ingsw.utils.IterableRange;
import it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper;
import it.polimi.ingsw.utils.streams.StreamUtils;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;
import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.wrap;

public class MatchManager implements PlayerEventsListener, MatchCommunicationsListener {

    private static Map<Integer, MatchManager> matchManagerMap = new HashMap<>();

    @SuppressWarnings("squid:S1602")
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
    private MatchObjectsManager matchObjectsManager;
    private RoundManager roundManager;

    // ------ MATCH OBJECTS CACHE ------
    private final List<DatabasePlayer> lobbyPlayers;
    private List<Window> possibleChosenWindows;
    private List<DatabasePlayer> matchPlayers;

    // ------ TIMERS AND FUTURES ------
    private ScheduledExecutorService matchExecutorService;
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
    /**
     * The rounds' {@link ExecutorService}.
     */
    private ExecutorService roundsExecutorService;
    /**
     * The {@link Future} that handles the rounds.
     */
    private Future<?> roundsTaskFuture;
    
    // ------ FSM OBJECTS ------
    private MatchState matchState = MatchState.WAITING_FOR_PLAYERS;

    public DatabaseMatch getDatabaseMatch() {
        return this.databaseMatch;
    }
    
    public MatchManager(int lobbyId) throws SQLException {
        this.databaseMatch = DatabaseMatch.insertMatchFromLobby(lobbyId);
        this.lobbyPlayers = Arrays.stream(DatabaseLobby.getLobbyWithId(lobbyId).getPlayers())
                .map(DatabasePlayer.class::cast)
                .collect(Collectors.toList());

        // creates the scheduled executor service
        this.matchExecutorService = Executors.newScheduledThreadPool(2);
        // creates the executor service
        this.roundsExecutorService = Executors.newSingleThreadExecutor();
        
        this.setUpConnectionTimer();

        this.matchCommunicationsManager = new MatchCommunicationsManager(
                this.databaseMatch.toMatchMock(),
                this
        );
        this.matchObjectsManager = MatchObjectsManager.getManagerForMatch(this.databaseMatch);
        this.matchPlayers = new ArrayList<>(this.lobbyPlayers.size());

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
    
    private boolean canAcceptPlayer(DatabasePlayer player) {
        return lobbyPlayers.contains(player) && this.matchState == MatchState.WAITING_FOR_PLAYERS || this.databaseMatch.getLeftPlayers().contains(player);
    }
    
    @SuppressWarnings("squid:S1450")
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
        RoundTrack roundTrack = new RoundTrack(Settings.getSettings().getNumberOfRounds());
        DraftPool draftPool = new DraftPool((byte) this.matchPlayers.size());
        ToolCard[] toolCards = getToolCards();
    
        RoundTrackControllerImpl roundTrackController = new RoundTrackControllerImpl(roundTrack);
        DraftPoolControllerImpl draftPoolController = new DraftPoolControllerImpl(draftPool);
        ToolCardControllerImpl[] toolCardControllers = Arrays.stream(toolCards)
                .map((ToolCard toolCard) -> new ToolCardControllerImpl(toolCard, this.databaseMatch.getId(), this.matchObjectsManager))
                .toArray(ToolCardControllerImpl[]::new);
        
        this.matchObjectsManager.setRoundTrackController(roundTrackController);
        this.matchObjectsManager.setDraftPoolController(draftPoolController);
        this.matchObjectsManager.setToolCardControllers(toolCardControllers);

        draftPoolController.putAll(
                IntStream.range(0, 2 * this.matchPlayers.size() + 1)
                        .mapToObj(i -> bag.pickDie())
                        .toArray(Die[]::new)
        );
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
    
    // on expiration turn is over and the next player is awaken
    private final Runnable moveTimerExecutorTask = () -> this.roundManager.current().end();
    
    private final Runnable roundsExecutorTask = () -> {
        MatchContextHelper.init(
                GlobalContext.getGlobalContext().getContextForMatch(this.databaseMatch.getId()),
                this.databaseMatch,
                this.matchObjectsManager,
                this.roundManager.getPlayerTurnList()
        );

        Turn turn = this.roundManager.next();
        byte lastRound = turn.getRound();
    
        try {
            MatchContextHelper.getContextForPlayer(
                    GlobalContext.getGlobalContext().getContextForMatch(this.databaseMatch.getId()),
                    turn.getPlayer()
            ).put(Context.CURRENT_TURN, turn);
            
            do {
                this.matchCommunicationsManager.notifyPlayerTurnBegin(
                        turn.getPlayer(),
                        (int) Settings.getSettings().getMatchMoveTimerTimeUnit().toSeconds(
                                Settings.getSettings().getMatchMoveTimerDuration()
                        )
                );
                this.moveTimerScheduledFuture = this.matchExecutorService.schedule(
                        this.moveTimerExecutorTask,
                        Settings.getSettings().getMatchMoveTimerDuration(),
                        Settings.getSettings().getMatchMoveTimerTimeUnit()
                );
                
                turn.waitUntilEnded();
                
                if (!this.moveTimerScheduledFuture.isCancelled()) {
                    this.moveTimerScheduledFuture.cancel(true);
                }
    
                this.matchCommunicationsManager.notifyPlayerTurnEnd(turn.getPlayer());
                
                turn = this.roundManager.next();
                
                if (turn != null && lastRound != turn.getRound()) {
                    Die[] diceLeft = this.matchObjectsManager.getDraftPoolController().pickAllDiceLeft();
                    this.matchObjectsManager.getRoundTrackController().putAllInRound(lastRound, diceLeft);
    
                    this.matchObjectsManager.getDraftPoolController().putAll(
                            new IterableRange<>(
                                    1,
                                    2 * this.matchPlayers.size() + 1,
                                    IterableRange.INTEGER_INCREMENT_FUNCTION
                            ).stream()
                                    .map(integer -> this.matchObjectsManager.getBag().pickDie())
                                    .toArray(Die[]::new)
                    );
    
                    lastRound = turn.getRound();
                }
                
                this.onModelsUpdated();
            } while (turn != null);

            this.matchState = MatchState.CALCULATING_RESULTS;

            calculateAndSendResults();

            this.matchState = MatchState.CLOSING_MATCH;
        }
        catch (InterruptedException e) {
            ServerLogger.getLogger().log(Level.WARNING, "Error while waiting for turn to end", e);
            Thread.currentThread().interrupt();
        }
        catch (IOException e) {
            ServerLogger.getLogger().log(Level.SEVERE, "Error occurred while querying the database:", e);
        }
    };
    
    /**
     * Sets up the connection timer.
     */
    private void setUpConnectionTimer() {
        this.connectionTimerScheduledFuture = matchExecutorService.schedule(
                connectionTask,
                Settings.getSettings().getMatchConnectionTimerDuration(),
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

    private MoveResponse handleMove(Move move, DatabasePlayer databasePlayer, Turn currentTurn) throws IOException {
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
                        false,
                        false,
                        false
                );

                this.matchExecutorService.schedule(() -> {
                    try {
                        onModelsUpdated();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }, 150, TimeUnit.MILLISECONDS);

                onModelsUpdated();

                return new MoveResponse(this.databaseMatch.getId(), true);
            }
            catch (DieInteractionException e) {
                return new MoveResponse(this.databaseMatch.getId(), false);
            }
        }
        else {
            return new MoveResponse(this.databaseMatch.getId(), false);
        }
    }

    // ------ RESULTS HANDLING ------

    private void calculateAndSendResults() throws IOException {
        Map<DatabasePlayer, Integer> partialResults = ResultsManager.getPartialPointsForPlayers(
                this.databaseMatch.getId(),
                this.matchPlayers.toArray(new DatabasePlayer[0]),
                Arrays.stream(this.matchObjectsManager.getPublicObjectiveCards())
                        .map(ObjectiveCard::getObjective)
                        .toArray(Objective[]::new)
        );
        partialResults = ResultsManager.integratePartialResultsWithPrivateObjectives(
                this.databaseMatch.getId(),
                partialResults,
                this.matchObjectsManager.getPrivateObjectiveCards()
                        .entrySet().stream()
                        .map(entry -> Map.entry(entry.getKey(), ((Objective) entry.getValue().getObjective())))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
        partialResults = ResultsManager.finalizeWithPenaltiesAndBonus(
                this.databaseMatch.getId(),
                partialResults,
                this.matchObjectsManager
        );


        this.matchCommunicationsManager.sendResults(partialResults);
    }

    // ------ PLAYER EVENTS ------
    
    @Override
    @SuppressWarnings("squid:S1450")
    public void onPlayerDisconnected(DatabasePlayer player) {
        try {
            DatabaseMatch.removePlayer(this.databaseMatch.getId(), player.getId());
            this.matchCommunicationsManager.removePlayer(player);
            this.matchObjectsManager.setPlayersLeft(new HashSet<>(this.databaseMatch.getLeftPlayers()));
        }
        catch (SQLException e) {
            ServerLogger.getLogger().log(Level.SEVERE, "Error occurred while querying the database:", e);
            throw new RuntimeException(e);
        }
    }

    // ------ MODELS EVENTS ------

    public void onModelsUpdated() throws IOException {
        for (DatabasePlayer player : this.matchPlayers) {
            IMatch match = this.matchObjectsManager.buildMatchMockForPlayer(player);
            this.matchCommunicationsManager.sendMatchMockToPlayer(player, match);
        }
    }

    // ------ COMMUNICATIONS EVENTS ------

    @Override
    @SuppressWarnings("squid:S00112")
    public void onWindowChosen(MatchCommunicationsManager matchCommunicationsManager, DatabasePlayer databasePlayer, IWindow window) {
        Window chosenWindow = this.possibleChosenWindows.stream()
                .filter(w -> w.getId().equals(window.getId()))
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

                this.roundManager = RoundManager.createRoundManager(this.databaseMatch, Settings.getSettings().getNumberOfRounds());

                this.roundsTaskFuture = this.roundsExecutorService.submit(this.roundsExecutorTask);
            }
            catch (ClassNotFoundException e) {
                ServerLogger.getLogger().log(Level.SEVERE, "Error occurred while creating models and controllers:", e);
                throw new RuntimeException(e);
            }
            catch (IOException e) {
                ServerLogger.getLogger().log(Level.SEVERE, "Error occurred while sending mocks:", e);
            }
        }
    }

    @Override
    public MoveResponse onPlayerTriedToMove(MatchCommunicationsManager matchCommunicationsManager, DatabasePlayer databasePlayer, Move move) {
         if (this.roundManager.current().getPlayer().getId() != databasePlayer.getId()) {
            return new MoveResponse(databaseMatch.getId(), false);
        }

        Turn currentTurn = this.roundManager.current();

        try {
            MoveResponse moveResponse = this.handleMove(move, databasePlayer, currentTurn);
            currentTurn.appendPhase(Turn.DIE_PLACED);

            return moveResponse;
        }
        catch (IOException e) {
            ServerLogger.getLogger().log(Level.SEVERE, "Error while sending move response to user: " + databasePlayer.toString(), e);
            return null;
        }
    }
    
    @Override
    public void onPlayerEndRequest(MatchCommunicationsManager matchCommunicationsManager, DatabasePlayer databasePlayer) {
        if (this.roundManager.current().getPlayer().getId() == databasePlayer.getId()) {
            this.roundManager.current().end();
        }
    }
    
    @Override
    public void onPlayerRequestedToolCard(MatchCommunicationsManager matchCommunicationsManager, DatabasePlayer databasePlayer, IToolCard toolCard) {
        if (this.roundManager.current().getPlayer().getId() != databasePlayer.getId()) {
            return;
        }
    
        Optional<ToolCardControllerImpl> optionalController = Arrays.stream(this.matchObjectsManager.getToolCardControllers())
                .filter(toolCardController -> toolCardController.getToolCard().getCardId().equals(toolCard.getCardId()))
                .findFirst();
        
        if (!optionalController.isPresent()) {
            return;
        }
        
        ToolCardControllerImpl controller = optionalController.get();
        
        Integer tokens = this.matchObjectsManager.getFavourTokensMap().get(databasePlayer);
        
        if (!controller.canUse(tokens)) {
            throw new NotEnoughTokensException(
                    controller.getToolCard().getEffect().getCost(),
                    tokens
            );
        }
        
        controller.setUserInteractionProvider(
                new UserInteractionAndCallbacksManager(
                        databasePlayer,
                        this.matchObjectsManager,
                        this.matchCommunicationsManager
                )
        );
        
        controller.requestUsage(
                databasePlayer, tokens
        );

        controller.setUserInteractionProvider(null);
    }
    
    private enum MatchState {
        WAITING_FOR_PLAYERS,
        WAITING_FOR_WINDOW_RESPONSES,
        RUNNING_ROUNDS,
        CALCULATING_RESULTS,
        CLOSING_MATCH
    }
}
