package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.controllers.proxies.rmi.MatchRMIProxyController;
import it.polimi.ingsw.controllers.proxies.socket.MatchSocketProxyController;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.net.mocks.IWindow;
import it.polimi.ingsw.server.controllers.WindowControllerImpl;
import it.polimi.ingsw.server.sql.DatabasePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class MatchCommunicationsManager {

    /**
     * A {@link Map} containing the {@link MatchController} associated to a {@link DatabasePlayer}.
     */
    private final Map<DatabasePlayer, MatchController> playersHandler = new HashMap<>();

    public MatchCommunicationsManager(IMatch match) {

    }

    public void addPlayer(DatabasePlayer databasePlayer, MatchController matchController) {
        this.playersHandler.put(databasePlayer, matchController);
    }

    public void removePlayer(DatabasePlayer databasePlayer) {
        this.playersHandler.remove(databasePlayer);
    }

    public void sendWindowsToChoose(Map<IPlayer, IWindow[]> playerToWindowsMap) {
        playerToWindowsMap.forEach((player, iWindows) -> {
            playersHandler.get(player).postWindowsToChoose(iWindows);
        });
    }

    public void sendWindowController(WindowControllerImpl windowController) {
    }

    public void forEachRmi(BiConsumer<DatabasePlayer, MatchRMIProxyController> biConsumer) {
        this.playersHandler.forEach((databasePlayer, matchController) -> {
            if (matchController instanceof MatchRMIProxyController) {
                biConsumer.accept(databasePlayer, (MatchRMIProxyController) matchController);
            }
        });
    }

    public void forEachSocket(BiConsumer<DatabasePlayer, MatchSocketProxyController> biConsumer) {
        this.playersHandler.forEach((databasePlayer, matchController) -> {
            if (matchController instanceof MatchSocketProxyController) {
                biConsumer.accept(databasePlayer, (MatchSocketProxyController) matchController);
            }
        });
    }
}
