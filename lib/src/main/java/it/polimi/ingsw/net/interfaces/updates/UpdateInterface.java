package it.polimi.ingsw.net.interfaces.updates;

import java.rmi.Remote;

public interface UpdateInterface<T extends Remote> extends Remote {
    void onUpdateReceived(T update);
}
