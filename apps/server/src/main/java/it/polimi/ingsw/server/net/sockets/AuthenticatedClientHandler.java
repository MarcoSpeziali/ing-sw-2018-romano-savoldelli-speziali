package it.polimi.ingsw.server.net.sockets;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.server.Constants;
import it.polimi.ingsw.server.managers.AuthenticationManager;
import it.polimi.ingsw.server.net.commands.Command;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.utils.ServerLogger;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class AuthenticatedClientHandler extends ClientHandler {

    private static Map<DatabasePlayer, AuthenticatedClientHandler> clientHandlers = new HashMap<>();

    private Thread inputThread;
    private boolean shouldStop = false;
    
    public static AuthenticatedClientHandler getHandlerForPlayer(DatabasePlayer databasePlayer) {
        return clientHandlers.getOrDefault(databasePlayer, null);
    }
    
    public static AuthenticatedClientHandler migrate(AnonymousClientHandler clientHandler, DatabasePlayer databasePlayer, Request request) {
        return null;
    }

    public AuthenticatedClientHandler(ClientHandler clientHandler, DatabasePlayer player) throws IOException {
        this(clientHandler.client, player);
    }

    public AuthenticatedClientHandler(Socket client, DatabasePlayer player) throws IOException {
        super(client);
        clientHandlers.put(player, this);
    }

    @Override
    public void run() {
        try {
            this.inputThread = new Thread(() -> {
                Command handler;
    
                SocketAddress socketAddress = this.client.getRemoteSocketAddress();
    
                try {
                    do {
                        handler = handleIncomingRequest(AuthenticationManager::isAuthenticated, null);
            
                        // if the handler needs the connection to be kept alive it wont be closed
                    } while (handler != null && handler.shouldBeKeptAlive() && !shouldStop);
                }
                catch (Exception e) {
                    ServerLogger.getLogger(AnonymousClientHandler.class)
                            .log(Level.WARNING, "Error while handling client: " + socketAddress, e);
                }
            });
            this.inputThread.setName(Constants.Threads.CLIENT_INPUT_HANDLER.toString());
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
}
