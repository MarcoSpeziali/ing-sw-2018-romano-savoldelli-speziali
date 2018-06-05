package it.polimi.ingsw.client.net.endpoints;

import it.polimi.ingsw.client.Settings;
import it.polimi.ingsw.net.LobbyInfo;
import it.polimi.ingsw.net.interfaces.LobbyLookupInterface;
import it.polimi.ingsw.net.utils.EndPointFunction;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.function.Consumer;

public class RMILobbyLookup implements LobbyLookupInterface {

    private static Registry registry;
    private static LobbyLookupInterface lobbyLookupInterface;

    public RMILobbyLookup() throws RemoteException, NotBoundException {
        if (registry == null) {
            registry = LocateRegistry.getRegistry(Settings.getSettings().getServerRMIAddress(), Settings.getSettings().getServerRMIPort());
            lobbyLookupInterface = (LobbyLookupInterface) registry.lookup(EndPointFunction.LOOK_UP.getEndPointFunctionName());
        }
    }
    @Override
    public void requestLookup(String token, Consumer<LobbyInfo> updateFunction, Consumer<Math> onStart) {
        lobbyLookupInterface.requestLookup(token, updateFunction, onStart);
    }
}
