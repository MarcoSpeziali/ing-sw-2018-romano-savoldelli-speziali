package it.polimi.ingsw.controllers.proxies;

import java.io.IOException;
import java.rmi.Remote;

public interface RemotelyInitializable extends Remote {
    void init(Object... args) throws IOException;
}
