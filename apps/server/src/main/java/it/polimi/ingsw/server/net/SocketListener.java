package it.polimi.ingsw.server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketListener implements AutoCloseable {

    private final int port;

    private boolean shouldStop = false;

    public SocketListener(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            while (!shouldStop) {
                Socket client = serverSocket.accept();

                new Thread(() -> {

                }).start();
            }
        }
    }

    @Override
    public void close() throws Exception {
        this.shouldStop = true;
    }
}
