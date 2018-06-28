package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.controllers.proxies.rmi.MatchRMIProxyController;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.mocks.IWindow;
import it.polimi.ingsw.net.mocks.WindowMock;
import it.polimi.ingsw.net.requests.WindowRequest;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.events.EventDispatcher;
import it.polimi.ingsw.server.events.EventType;
import it.polimi.ingsw.server.events.MatchCommunicationsListener;
import it.polimi.ingsw.server.net.sockets.AuthenticatedClientHandler;
import it.polimi.ingsw.server.net.sockets.middlewares.MatchControllerMiddleware;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class MatchCommunicationsManager {

    /**
     * A {@link Map} containing the {@link MatchRMIProxyController} associated to a {@link DatabasePlayer}.
     */
    private final Map<DatabasePlayer, MatchRMIProxyController> rmiPlayersHandler = new HashMap<>();

    /**
     * A {@link Map} containing the {@link AuthenticatedClientHandler} associated to a {@link DatabasePlayer}.
     */
    private final Map<DatabasePlayer, AuthenticatedClientHandler> socketPlayersHandler = new HashMap<>();
    private final IMatch match;
    private final MatchManager matchManager;
    
    public MatchCommunicationsManager(IMatch match, MatchManager matchManager) {
        this.match = match;
        this.matchManager = matchManager;
    }

    public void addPlayer(DatabasePlayer databasePlayer, MatchRMIProxyController matchController) {
        this.rmiPlayersHandler.put(databasePlayer, matchController);
    }
    
    public void addPlayer(DatabasePlayer databasePlayer, AuthenticatedClientHandler authenticatedClientHandler) {
        this.socketPlayersHandler.put(databasePlayer, authenticatedClientHandler);
    }

    public void removePlayer(DatabasePlayer databasePlayer) {
        this.rmiPlayersHandler.remove(databasePlayer);
        this.socketPlayersHandler.remove(databasePlayer);
    }

    public void closeConnectionToOtherPlayers() {
        this.forEachSocket((databasePlayer, authenticatedClientHandler) -> authenticatedClientHandler.addMiddleware(
                MatchControllerMiddleware.class,
                new MatchControllerMiddleware(this)
        ));
    }

    public void sendWindowsToChoose(Map<DatabasePlayer, IWindow[]> playerToWindowsMap) throws IOException {
        this.forEachRmi((databasePlayer, matchRMIProxyController) -> matchRMIProxyController.postWindowsToChoose(playerToWindowsMap.get(databasePlayer)));

        try {
            this.forEachSocket((databasePlayer, authenticatedClientHandler) -> {
                try {
                    authenticatedClientHandler.sendRequest(new Request<>(
                            new Header(EndPointFunction.MATCH_WINDOW_REQUEST),
                            new WindowRequest(
                                    this.match.getId(),
                                    Arrays.stream(playerToWindowsMap.get(databasePlayer))
                                            .map(WindowMock::new)
                                            .toArray(WindowMock[]::new)
                            )
                    ));
                }
                catch (IOException e) {
                    FunctionalExceptionWrapper.wrap(e);
                }
            });
        }
        catch (FunctionalExceptionWrapper e) {
            e.tryFinalUnwrap(IOException.class);
        }
    }

    /*public void sendWindowController(DatabasePlayer databasePlayer, WindowControllerImpl windowController) throws RemoteException {
        MatchController matchController = null;*//*= this.playersHandler.get(databasePlayer)*//*;
    
        final int rows = windowController.getWindow().getRows();
        final int columns = windowController.getWindow().getColumns();
    
        CellController[][] cellControllers = new CellController[rows][columns];
        WindowController controller;
    
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                CellControllerImpl actualController = windowController.getCellController(i, j);
                
                if (matchController instanceof MatchRMIProxyController) {
                    cellControllers[i][j] = new CellRMIProxyController(
                            new CellMock(actualController.getCell()),
                            ControllersMapper.map(actualController, matchManager)
                    );
                }
                else {
                
                }
            }
        }
        
        if (matchController instanceof MatchRMIProxyController) {
            controller = new WindowRMIProxyController(
                    new WindowMock(windowController.getWindow()),
                    ControllersMapper.map(windowController, cellControllers, matchManager)
            );
        }
        else {
        
        }
    }*/

    public void forEachRmi(BiConsumer<DatabasePlayer, MatchRMIProxyController> biConsumer) {
        this.rmiPlayersHandler.forEach(biConsumer);
    }

    public void forEachSocket(BiConsumer<DatabasePlayer, AuthenticatedClientHandler> biConsumer) {
        this.socketPlayersHandler.forEach(biConsumer);
    }

    public void onWindowChosen(DatabasePlayer databasePlayer, IWindow chosenWindow) {
        EventDispatcher.dispatch(
                EventType.MATCH_COMMUNICATION_EVENTS,
                MatchCommunicationsListener.class,
                matchCommunicationsListener -> matchCommunicationsListener.onWindowChosen(this, databasePlayer, chosenWindow)
        );
    }
}
