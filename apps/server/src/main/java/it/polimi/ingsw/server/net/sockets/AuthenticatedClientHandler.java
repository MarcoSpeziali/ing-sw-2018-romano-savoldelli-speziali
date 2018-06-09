package it.polimi.ingsw.server.net.sockets;

import it.polimi.ingsw.server.sql.DatabasePlayer;

import java.io.IOException;
import java.net.Socket;

public class AuthenticatedClientHandler extends ClientHandler {

    private final DatabasePlayer player;

    public AuthenticatedClientHandler(ClientHandler clientHandler, DatabasePlayer player) throws IOException {
        this(clientHandler.client, player);
    }

    public AuthenticatedClientHandler(Socket client, DatabasePlayer player) throws IOException {
        super(client);

        this.player = player;
    }

    @Override
    public void run() {

    }
}
