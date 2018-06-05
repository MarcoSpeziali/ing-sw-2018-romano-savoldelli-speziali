package it.polimi.ingsw.server.net.endpoints;

import it.polimi.ingsw.net.LobbyInfo;
import it.polimi.ingsw.net.interfaces.LobbyLookupInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.function.Consumer;

public class LobbyLookupEndPoint extends UnicastRemoteObject implements LobbyLookupInterface {

    private static final long serialVersionUID = 7818246236195800247L;
    private static LobbyLookupEndPoint instance;

    static {
        try {
            instance = new LobbyLookupEndPoint();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public static LobbyLookupEndPoint getInstance() {
        return instance;
    }

    public LobbyLookupEndPoint() throws RemoteException {
    }

    @Override
    public void requestLookup(String token, Consumer<LobbyInfo> updateFunction, Consumer<Math> onStart) {
        // TODO implement
    }
}
