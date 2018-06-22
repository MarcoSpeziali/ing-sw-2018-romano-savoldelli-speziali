package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.server.sql.DatabaseLobby;

public class LobbyManager {

    private DatabaseLobby databaseLobby;

    public LobbyManager(DatabaseLobby databaseLobby) {
        this.databaseLobby = databaseLobby;
    }
}
