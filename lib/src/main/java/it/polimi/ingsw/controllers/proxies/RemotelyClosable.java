package it.polimi.ingsw.controllers.proxies;

import java.io.IOException;
import java.rmi.Remote;

public interface RemotelyClosable extends Remote {
    void close() throws IOException;
}
