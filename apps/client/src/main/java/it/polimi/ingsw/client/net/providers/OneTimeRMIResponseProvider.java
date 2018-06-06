package it.polimi.ingsw.client.net.providers;

import it.polimi.ingsw.client.Settings;
import it.polimi.ingsw.net.Body;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.RespondsTo;
import it.polimi.ingsw.utils.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class OneTimeRMIResponseProvider<T extends Remote> implements OneTimeNetworkResponseProvider {

    private final Class<T> remoteInterfaceType;

    public OneTimeRMIResponseProvider(Class<T> remoteInterfaceType) {
        this.remoteInterfaceType = remoteInterfaceType;
    }

    /**
     * Sends a {@link Request} to the server and waits for a {@link Response} (blocking call).
     * Then the connection is terminated.
     *
     * @param request     the {@link Request} to send
     * @param hostAddress the address of the host
     * @param hostPort    the port of the host
     * @return a {@link Response} produced by the server
     * @throws IOException       if any IO error occurs
     * @throws NotBoundException if {@link Body#getEndPointFunction()} is not currently bound
     * @throws ReflectiveOperationException if a reflection error occurs
     */
    @Override
    public Response getSyncResponseFor(Request request, String hostAddress, int hostPort) throws IOException, NotBoundException, ReflectiveOperationException {
        Method targetMethod = ReflectionUtils.findAnnotatedMethod(
                remoteInterfaceType,
                RespondsTo.class,
                respondsTo -> respondsTo.value().equals(request.getBody().getEndPointFunction())
        );

        if (targetMethod == null) {
            throw new NotBoundException();
        }

        Registry registry = LocateRegistry.getRegistry(hostAddress, hostPort);
        Remote remoteInterface = registry.lookup(request.getBody().getEndPointFunction().toString());

        return (Response) targetMethod.invoke(remoteInterface, request);
    }

    /**
     * Sends a {@link Request} to the server.
     * The connection is opened using the default address and port (from {@link Settings}).
     * Then the connection is terminated.
     *
     * @param request the {@link Request} to send
     * @return a {@link Response} produced by the server
     * @throws IOException       if any IO error occurs
     * @throws NotBoundException if {@link Body#getEndPointFunction()} is not currently bound
     * @throws ReflectiveOperationException if a reflection error occurs
     */
    @Override
    public Response getSyncResponseFor(Request request) throws IOException, NotBoundException, ReflectiveOperationException {
        return getSyncResponseFor(
                request,
                Settings.getSettings().getServerRMIAddress(),
                Settings.getSettings().getServerRMIPort()
        );
    }
}
