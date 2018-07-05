package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONSerializable;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LobbyMockTest {
    private Long opening = 2147483648L;
    private Long closing = 2147483950L;
    private PlayerMock playerMock1 = new PlayerMock(1, "prova1");
    private PlayerMock playerMock2 = new PlayerMock(2, "prova2");
    private PlayerMock[] playerMocks = new PlayerMock[] { playerMock1, playerMock2 };


    private LobbyMock lobbyMock = new LobbyMock(32,opening, closing, 15, playerMocks);

    @Test
    void testSerializationAndDeserialization(){
        JSONObject jsonObject = lobbyMock.serialize();
        LobbyMock deserializedLobbyMock = JSONSerializable.deserialize(LobbyMock.class, jsonObject);
        Assertions.assertEquals(lobbyMock.getClosingTime(), deserializedLobbyMock.getClosingTime());
        for (int i = 0; i < playerMocks.length ; i++) {
            Assertions.assertEquals(lobbyMock.getPlayers()[i].getId(), deserializedLobbyMock.getPlayers()[i].getId());
        }

    }

    @Test
    void getId() {
        Assertions.assertEquals(32, lobbyMock.getId());

    }

    @Test
    void getOpeningTime() {
        Assertions.assertEquals(2147483648L, lobbyMock.getOpeningTime());
    }

    @Test
    void getClosingTime() {
        Assertions.assertEquals(2147483950L, lobbyMock.getClosingTime());
    }

    @Test
    void getTimeRemaining() {
        Assertions.assertEquals(15, lobbyMock.getTimeRemaining());
    }

    @Test
    void getPlayers() {
        for (int i = 0; i < playerMocks.length; i++) {
            Assertions.assertEquals(playerMock1, lobbyMock.getPlayers()[0]);
        }
    }
}