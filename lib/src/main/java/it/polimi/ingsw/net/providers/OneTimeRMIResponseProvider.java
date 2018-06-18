package it.polimi.ingsw.net.providers;

import it.polimi.ingsw.net.Body;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.EndPointResponderNotFound;
import it.polimi.ingsw.net.interfaces.RespondsTo;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.utils.ReflectionUtils;
import it.polimi.ingsw.utils.io.JSONSerializable;

import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class OneTimeRMIResponseProvider<R extends Remote> implements OneTimeNetworkResponseProvider {

    private final Class<R> remoteInterfaceType;
    private final String rmiAddress;
    private final int rmiPort;

    public OneTimeRMIResponseProvider(String rmiAddress, int rmiPort, Class<R> remoteInterfaceType) {
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
        Remote remoteInterface = registry.lookup(getRMIEndPointName(request.getHeader().getEndPointFunction()));

        try {
            //noinspection unchecked
            return (Response<T>) targetMethod.invoke(remoteInterface, request);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getSyncRemoteObject(EndPointFunction endPointFunction, String hostAddress, int hostPort, Object... args) throws RemoteException, NotBoundException {
        Method targetMethod = ReflectionUtils.findAnnotatedMethod(
                remoteInterfaceType,
                RespondsTo.class,
                respondsTo -> respondsTo.value().equals(endPointFunction)
        );

        if (targetMethod == null) {
            throw new EndPointResponderNotFound(endPointFunction, this.remoteInterfaceType);
        }

        Registry registry = LocateRegistry.getRegistry(hostAddress, hostPort);
        Remote remoteInterface = registry.lookup(getRMIEndPointName(endPointFunction));

        try {
            //noinspection unchecked
            return (T) targetMethod.invoke(remoteInterface, args);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getSyncRemoteObject(EndPointFunction endPointFunction, Object... args) throws RemoteException, NotBoundException {
        return this.getSyncRemoteObject(
                endPointFunction,
                getServerAddress(),
                getServerPort(),
                args
        );
    }

    /**
     * Returns the name for the specified {@link EndPointFunction}. (//$host:$port/$endpoint)
     *
     * @param endPointFunction the {@link EndPointFunction}
     * @return the name for the specified {@link EndPointFunction}. (//$host:$port/$endpoint)
     */
    private String getRMIEndPointName(EndPointFunction endPointFunction) {
        return String.format(
                "//%s:%d/%s",
                getServerAddress(),
                getServerPort(),
                endPointFunction.toString()
        );
    }
}
