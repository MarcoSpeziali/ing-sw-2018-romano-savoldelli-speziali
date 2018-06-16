package it.polimi.ingsw.client.net;

public class SagradaServer {

    private static SagradaServer instance = new SagradaServer();

    private SagradaServer() {
    }

    public static SagradaServer getInstance() {
        return instance;
    }


}
