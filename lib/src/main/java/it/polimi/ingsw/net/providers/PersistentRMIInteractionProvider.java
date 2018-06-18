package it.polimi.ingsw.net.providers;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.RespondsTo;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.utils.ReflectionUtils;
import it.polimi.ingsw.utils.io.JSONSerializable;

import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

// TODO: docs
public class PersistentRMIInteractionProvider<R extends Remote> extends PersistentNetworkInteractionProvider {

    private final Class<R> remoteInterfaceType;

    private Remote remoteInterface;

    public PersistentRMIInteractionProvider(String remoteAddress, int remotePort, Class<R> remoteInterfaceType) {
        super(remoteAddress, remotePort);
        this.remoteInterfaceType = remoteInterfaceType;
    }

    @Override
    public void open(EndPointFunction remoteEndPoint) throws IOException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(this.remoteAddress, this.remotePort);
        this.remoteInterface = registry.lookup(getRMIEndPointName(remoteEndPoint));
    }

    @Override
    public <T extends JSONSerializable, K extends JSONSerializable> Response<T> getSyncResponseFor(Request<K> request) throws IOException, NotBoundException, ReflectiveOperationException {
        return getSyncResponseFor(request, new Object());
    }

    public <T extends JSONSerializable, K extends JSONSerializable> Response<T> getSyncResponseFor(Request<K> request, Object... args) throws ReflectiveOperationException, NotBoundException {
        Method targetMethod = ReflectionUtils.findAnnotatedMethod(
                this.remoteInterfaceType,
                RespondsTo.class,
                respondsTo -> respondsTo.value().equals(request.getHeader().getEndPointFunction())
        );

        if (targetMethod == null) {
            throw new NotBoundException();
        }

        Object[] arguments = new Object[1 + args.length];
        arguments[0] = request;

        System.arraycopy(args, 0, arguments, 1, args.length);

        Object returnValue = targetMethod.invoke(this.remoteInterface, arguments);

        if (returnValue == null) {
            return null;
        }

        //noinspection unchecked
        return (Response<T>) returnValue;
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
                this.remoteAddress,
                this.remotePort,
                endPointFunction.toString()
        );
    }

    @Override
    public void close() throws IOException {
        // no need to close the connection
    }
}
