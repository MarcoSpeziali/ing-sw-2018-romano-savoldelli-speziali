package it.polimi.ingsw.net.interfaces;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;

import java.rmi.Remote;

public interface LobbyInterface extends Remote {

    Response joinLobby(Request request);


}
