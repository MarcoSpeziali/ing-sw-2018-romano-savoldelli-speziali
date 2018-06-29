package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONSerializable;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/*
class MatchMockTest {
    
    private Long opening = 2123L;
    private Long closing = 3123531L;
    private PlayerMock playerMock1 = new PlayerMock(1, "prova1");
    private PlayerMock playerMock2 = new PlayerMock(2, "prova2");
    private PlayerMock[] playerMocks = new PlayerMock[] { playerMock1, playerMock2 };
    private LivePlayerMock livePlayerMock1 = new LivePlayerMock(playerMock1);
    private LivePlayerMock livePlayerMock2 = new LivePlayerMock(playerMock2);
    private LivePlayerMock[] livePlayerMocks = new LivePlayerMock[] {livePlayerMock1, livePlayerMock2};
    private LobbyMock lobbyMock = new LobbyMock(32,opening, closing, 15, playerMocks);
    private MatchMock matchMock = new MatchMock(21, opening, closing, lobbyMock, new LivePlayerMock[0]);

    @Test
    void testSerializationAndDeserialization() {
        JSONObject jsonObject = matchMock.serialize();
        MatchMock deserializeMatchMock = JSONSerializable.deserialize(MatchMock.class, jsonObject);
        Assertions.assertEquals(3123531L, deserializeMatchMock.getEndingTime());
    }

    @Test
    void getId() {
        Assertions.assertEquals(21, matchMock.getId());
    }

    @Test
    void getStartingTime() {
        Assertions.assertEquals(2123L, matchMock.getStartingTime());
    }

    @Test
    void getEndingTime() {
        Assertions.assertEquals(3123531L, matchMock.getEndingTime());
    }

    @Test
    void getLobby() {
        Assertions.assertEquals(32, matchMock.getLobby().getId());
    }
    
    @Test
    void getPlayers() {
        for (int i = 0; i < playerMocks.length; i++) {
            Assertions.assertEquals(livePlayerMock1.getPlayer().getUsername(), matchMock.getPlayers()[0].getPlayer().getUsername());
        }
    }
}
*/