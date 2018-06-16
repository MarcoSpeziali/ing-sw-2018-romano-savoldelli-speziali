package it.polimi.ingsw.net.providers;

import it.polimi.ingsw.net.Body;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.EndPointResponderNotFound;
import it.polimi.ingsw.net.interfaces.RespondsTo;
import it.polimi.ingsw.utils.ReflectionUtils;
import it.polimi.ingsw.utils.io.JSONSerializable;

import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class OneTimeRMIResponseProvider<R extends Remote> implements OneTimeNetworkResponseProvider {

    private final Class<R> remoteInterfaceType;
    private final String rmiAddress;
    private final int rmiPort;

    public OneTimeRMIResponseProvider(Class<R> remoteInterfaceType, String rmiAddress, int rmiPort) {
        this.remoteInterfaceType = remoteInterfaceType;
        this.rmiAddress = rmiAddress;
        this.rmiPort = rmiPort;
    }

    @Override
    public String getServerAddress() {
        return this.rmiAddress;
    }

    @Override
    public int getServerPort() {
        return this.rmiPort;
    }

    /**
     * Sends a {@link Request} to the server and waits for a {@link Response} (blocking call).
     * Then the connection is terminated.
     *
     * @param request     the {@link Request} to send
     * @param hostAddress the address of the host
     * @param hostPort    the port of the host
     * @return a {@link Response} produced by the server
     * @throws IOException                  if any IO error occurs
     * @throws NotBoundException            if {@link Body#getEndPointFunction()} is not currently bound
     * @throws ReflectiveOperationException if a reflection error occurs
     */
    @Override
    public <T extends JSONSerializable, K extends JSONSerializable> Response<T> getSyncResponseFor(Request<K> request, String hostAddress, int hostPort) throws IOException, NotBoundException {
        Method targetMethod = ReflectionUtils.findAnnotatedMethod(
                remoteInterfaceType,
                RespondsTo.class,
                respondsTo -> respondsTo.value().equals(request.getHeader().getEndPointFunction())
        );

        if (targetMethod == null) {
            throw new EndPointResponderNotFound(request.getHeader().getEndPointFunction(), this.remoteInterfaceType);
        }

        Registry registry = LocateRegistry.getRegistry(hostAddress, hostPort);
        Remote remoteInterface = registry.lookup(request.getHeader().getEndPointFunction().toString());

        try {
            //noinspection unchecked
            return (Response<T>) targetMethod.invoke(remoteInterface, request);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
