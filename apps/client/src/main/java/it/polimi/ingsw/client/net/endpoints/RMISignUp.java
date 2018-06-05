package it.polimi.ingsw.client.net.endpoints;

import it.polimi.ingsw.client.Settings;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.SignUpInterface;
import it.polimi.ingsw.net.utils.EndPointFunction;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMISignUp implements SignUpInterface {

    private static Registry registry;
    private static SignUpInterface signUpInterface;

    public RMISignUp() throws RemoteException, NotBoundException {
        if (registry == null) {
            registry = LocateRegistry.getRegistry(Settings.getSettings().getServerRMIAddress(), Settings.getSettings().getServerRMIPort());
            signUpInterface = (SignUpInterface) registry.lookup(EndPointFunction.SIGN_UP.getEndPointFunctionName());
        }
    }

    @Override
    public Response requestSignUp(Request request) throws RemoteException {
        return signUpInterface.requestSignUp(request);
    }
}
