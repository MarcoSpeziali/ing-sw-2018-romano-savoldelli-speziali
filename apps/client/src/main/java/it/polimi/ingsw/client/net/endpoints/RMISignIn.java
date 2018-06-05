package it.polimi.ingsw.client.net.endpoints;

import it.polimi.ingsw.client.Settings;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.SignInInterface;
import it.polimi.ingsw.net.utils.EndPointFunction;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMISignIn implements SignInInterface {

    private static Registry registry;
    private static SignInInterface signInInterface;

    public RMISignIn() throws RemoteException, NotBoundException {
        if (registry == null) {
            registry = LocateRegistry.getRegistry(Settings.getSettings().getServerRMIAddress(), Settings.getSettings().getServerRMIPort());
            signInInterface = (SignInInterface) registry.lookup(EndPointFunction.SIGN_IN.getEndPointFunctionName());
        }
    }

    @Override
    public Response requestLogin(Request request) throws RemoteException {
        return signInInterface.requestLogin(request);
    }

    @Override
    public Response fulfillChallenge(Request request) throws RemoteException {
        return signInInterface.fulfillChallenge(request);
    }
}
