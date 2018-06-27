package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.controllers.CellController;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.controllers.WindowController;
import it.polimi.ingsw.controllers.proxies.rmi.CellRMIProxyController;
import it.polimi.ingsw.controllers.proxies.rmi.MatchRMIProxyController;
import it.polimi.ingsw.controllers.proxies.rmi.WindowRMIProxyController;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.mocks.CellMock;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.mocks.IWindow;
import it.polimi.ingsw.net.mocks.WindowMock;
import it.polimi.ingsw.net.requests.WindowRequest;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.controllers.CellControllerImpl;
import it.polimi.ingsw.server.controllers.WindowControllerImpl;
import it.polimi.ingsw.server.net.sockets.AuthenticatedClientHandler;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.utils.ControllersMapper;

import java.rmi.RemoteException;
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

    public void sendWindowsToChoose(Map<DatabasePlayer, IWindow[]> playerToWindowsMap) {
        this.forEachRmi((databasePlayer, matchRMIProxyController) -> matchRMIProxyController.postWindowsToChoose(playerToWindowsMap.get(databasePlayer)));
        
        this.forEachSocket((databasePlayer, authenticatedClientHandler) -> authenticatedClientHandler.sendRequest(new Request<>(
                new Header(EndPointFunction.MATCH_WINDOW_REQUEST),
                new WindowRequest(
                        this.match.getId(),
                        Arrays.stream(playerToWindowsMap.get(databasePlayer))
                                .map(WindowMock::new)
                                .toArray(WindowMock[]::new)
                )
        )));
    }

    public void sendWindowController(DatabasePlayer databasePlayer, WindowControllerImpl windowController) throws RemoteException {
        MatchController matchController = null;/*= this.playersHandler.get(databasePlayer)*/;
    
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
    }

    public void forEachRmi(BiConsumer<DatabasePlayer, MatchRMIProxyController> biConsumer) {
        this.rmiPlayersHandler.forEach(biConsumer);
    }

    public void forEachSocket(BiConsumer<DatabasePlayer, AuthenticatedClientHandler> biConsumer) {
        this.socketPlayersHandler.forEach(biConsumer);
    }
}
