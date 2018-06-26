package it.polimi.ingsw.server.net.sockets;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.server.Constants;
import it.polimi.ingsw.server.events.EventDispatcher;
import it.polimi.ingsw.server.events.EventType;
import it.polimi.ingsw.server.events.PlayerEventsListener;
import it.polimi.ingsw.server.utils.AuthenticationHelper;
import it.polimi.ingsw.server.net.commands.Command;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

// TODO: docs
public class AuthenticatedClientHandler extends ClientHandler {

    private static Map<DatabasePlayer, AuthenticatedClientHandler> clientHandlers = new HashMap<>();

    private Thread inputThread;
    private boolean shouldStop = false;
    private Request<? extends JSONSerializable> migrationRequest;
    private DatabasePlayer player;

    public AuthenticatedClientHandler(ClientHandler clientHandler, DatabasePlayer player) throws IOException {
        this(clientHandler.client, player);
    }

    public AuthenticatedClientHandler(Socket client, DatabasePlayer player) throws IOException {
        super(client);

        this.player = player;

        clientHandlers.put(player, this);
    }

    public static AuthenticatedClientHandler getHandlerForPlayer(DatabasePlayer databasePlayer) {
        return clientHandlers.getOrDefault(databasePlayer, null);
    }

    public static List<AuthenticatedClientHandler> getHandlerForPlayersExcept(DatabasePlayer databasePlayer) {
        return clientHandlers.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(databasePlayer))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public static AuthenticatedClientHandler migrate(AnonymousClientHandler clientHandler, DatabasePlayer databasePlayer, Request<? extends JSONSerializable> request) throws IOException {
        AuthenticatedClientHandler authenticatedClientHandler = new AuthenticatedClientHandler(clientHandler, databasePlayer);
        authenticatedClientHandler.migrationRequest = request;
        return authenticatedClientHandler;
    }

    public DatabasePlayer getPlayer() {
        return player;
    }

    @Override
    public void run() {
        try {
            this.inputThread = new Thread(() -> {
                Command handler;

                SocketAddress socketAddress = this.client.getRemoteSocketAddress();

                ServerLogger.getLogger()
                        .finer(() -> String.format("Handling client %s associated to player '%s'", socketAddress, this.player.getUsername()));

                EventDispatcher.dispatch(
                        EventType.PLAYER_EVENTS,
                        PlayerEventsListener.class,
                        playerEventsListener -> playerEventsListener.onPlayerConnected(this.player)
                );

                try {
                    do {
                        if (this.migrationRequest != null) {
                            handler = handleMigrationRequest(this.migrationRequest, AuthenticationHelper::isAuthenticated);
                            this.migrationRequest = null;
                        }
                        else {
                            handler = handleIncomingRequest(AuthenticationHelper::isAuthenticated);
                        }

                        // if the handler needs the connection to be kept alive it wont be closed
                    } while (handler != null && handler.shouldBeKeptAlive() && !shouldStop);
                }
                catch (Exception e) {
                    ServerLogger.getLogger()
                            .log(Level.WARNING, "Error while handling client: " + socketAddress, e);

                    clientHandlers.remove(this.player);

                    EventDispatcher.dispatch(
                            EventType.PLAYER_EVENTS,
                            PlayerEventsListener.class,
                            playerEventsListener -> playerEventsListener.onPlayerDisconnected(this.player)
                    );
                }
            });
            this.inputThread.setName(Constants.Threads.PLAYER_INPUT_HANDLER.toString() + "-" + this.player.toString());
            this.inputThread.start();
            this.inputThread.join();
        }
        catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void close() throws IOException {
        this.shouldStop = true;
        this.inputThread.interrupt();

        super.close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuthenticatedClientHandler that = (AuthenticatedClientHandler) o;
        return Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {

        return Objects.hash(player);
    }
}
