package it.polimi.ingsw.server.notifiers;

import it.polimi.ingsw.net.interfaces.updates.LobbyUpdatesInterface;
import it.polimi.ingsw.server.events.EventDispatcher;
import it.polimi.ingsw.server.events.LobbyEventsListener;
import it.polimi.ingsw.server.net.sockets.AnonymousClientHandler;
import it.polimi.ingsw.server.net.sockets.AuthenticatedClientHandler;
import it.polimi.ingsw.server.sql.DatabaseLobby;
import it.polimi.ingsw.server.sql.DatabasePlayer;

import java.util.HashMap;
import java.util.Map;

// TODO: check if needed and complete implementation if so
public class LobbyEventsNotifier implements LobbyEventsListener, Notifier<LobbyUpdatesInterface> {

    private static LobbyEventsNotifier instance = new LobbyEventsNotifier();

    public static LobbyEventsNotifier getInstance() {
        return instance;
    }

    private Map<DatabasePlayer, LobbyUpdatesInterface> rmiUpdatesInterfaces = new HashMap<>();
    private Map<DatabasePlayer, AuthenticatedClientHandler> socketUpdatesInterfaces = new HashMap<>();

    private LobbyEventsNotifier() {
        EventDispatcher.register(this);
    }

    @Override
    public void registerPlayerForUpdates(DatabasePlayer player, LobbyUpdatesInterface updateInterface) {
        if (updateInterface == null) {
            this.socketUpdatesInterfaces.put(player, AuthenticatedClientHandler.getHandlerForPlayer(player));
        }
        else {
            this.rmiUpdatesInterfaces.put(player, updateInterface);
        }
    }

    @Override
    public void unregisterPlayerForUpdates(DatabasePlayer player) {
        this.rmiUpdatesInterfaces.remove(player);
        this.socketUpdatesInterfaces.remove(player);
    }

    @Override
    public void onPlayerJoined(DatabaseLobby lobby, DatabasePlayer player) {

    }

    @Override
    public void onPlayerLeft(DatabaseLobby lobby, DatabasePlayer player) {
        unregisterPlayerForUpdates(player);
    }
}
