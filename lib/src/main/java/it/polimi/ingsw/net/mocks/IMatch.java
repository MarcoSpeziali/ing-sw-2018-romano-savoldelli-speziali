package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.rmi.Remote;
import java.util.List;

public interface IMatch extends JSONSerializable, Remote {

    int getId();
    long getStartingTime();
    long getEndingTime();
    ILobby getLobby();
    List<IPlayer> getPlayers();
}
