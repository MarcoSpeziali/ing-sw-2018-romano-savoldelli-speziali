package it.polimi.ingsw.net.interfaces;

import it.polimi.ingsw.net.utils.EndPointFunction;

import java.rmi.Remote;

public class EndPointResponderNotFound extends RuntimeException {

    public EndPointResponderNotFound(EndPointFunction endPointFunction, Class<? extends Remote> remoteInterface) {
        super(String.format("Could not find a method that responds to \"%s\" in the remote interface: %s", endPointFunction.toString(), remoteInterface.getName()));
    }
}
