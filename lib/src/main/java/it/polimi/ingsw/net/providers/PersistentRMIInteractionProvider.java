package it.polimi.ingsw.net.providers;

import it.polimi.ingsw.net.Body;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

// TODO: complete to match the future implementation of rmi server to client requests
public class PersistentRMIInteractionProvider<T extends Remote> extends PersistentNetworkInteractionProvider {

    private final Class<T> remoteInterfaceType;

    private Registry registry;
    private Remote remoteInterface;

    protected PersistentRMIInteractionProvider(String remoteAddress, int remotePort, Class<T> remoteInterfaceType) {
        super(remoteAddress, remotePort);

        this.remoteInterfaceType = remoteInterfaceType;
    }

    public void open(String remoteInterface) throws IOException, NotBoundException {
        this.registry = LocateRegistry.getRegistry(this.remoteAddress, this.remotePort);
        this.remoteInterface = this.registry.lookup(remoteInterface);
    }

    /**
     * Sends a {@link Request} to the server and waits for a {@link Response} (blocking call).
     *
     * @param request the {@link Request} to send
     * @return a {@link Response} produced by the server
     * @throws IOException                  if any IO error occurs
     * @throws NotBoundException            if {@link Body#getEndPointFunction()} is not currently bound
     * @throws ReflectiveOperationException if a reflection error occurs
     */
    @Override
    public Response getSyncResponseFor(Request request) throws IOException, NotBoundException, ReflectiveOperationException {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
