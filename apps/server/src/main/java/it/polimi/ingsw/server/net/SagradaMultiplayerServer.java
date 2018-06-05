package it.polimi.ingsw.server.net;

import it.polimi.ingsw.server.utils.ServerLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class SagradaMultiplayerServer implements Runnable, AutoCloseable {

    private final ServerSocket serverSocket;
    private final ExecutorService executorService;
    private boolean closed = false;

    public SagradaMultiplayerServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.executorService = Executors.newCachedThreadPool();

        ServerLogger.getLogger(SagradaMultiplayerServer.class)
                .log(Level.INFO, "Listening on port {0}", String.valueOf(port));
    }

    @Override
    public void run() {
        while (!closed) {
            try {
                Socket client = this.serverSocket.accept();

                ServerLogger.getLogger(SagradaMultiplayerServer.class)
                        .info("New client with address: " + client.getRemoteSocketAddress());

                this.executorService.submit(new ClientHandler(client));
            }
            catch (IOException e) {
                ServerLogger.getLogger(SagradaMultiplayerServer.class)
                        .log(Level.SEVERE, "I/O error waiting for players", e);
            }
        }

        try {
            this.close();
        }
        catch (IOException e1) {
            ServerLogger.getLogger(SagradaMultiplayerServer.class)
                    .log(Level.SEVERE, "I/O error while closing the server socket and the executor service", e1);

            throw new RuntimeException(e1);
        }
    }

    @Override
    public void close() throws IOException {
        this.closed = true;

        this.serverSocket.close();
        this.executorService.shutdown();
    }
}
