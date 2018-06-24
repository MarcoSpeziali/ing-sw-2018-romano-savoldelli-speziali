package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultMockTest {
    private Long opening = 2123L;
    private Long closing = 3123531L;
    private PlayerMock playerMock1 = new PlayerMock(1, "prova1");
    private PlayerMock playerMock2 = new PlayerMock(2, "prova2");
    private LivePlayerMock livePlayerMock1 = new LivePlayerMock(playerMock1);
    private LivePlayerMock livePlayerMock2 = new LivePlayerMock(playerMock2);
    private LivePlayerMock[] livePlayerMocks = new LivePlayerMock[] {livePlayerMock1, livePlayerMock2};
    private PlayerMock[] playerMocks = new PlayerMock[] { playerMock1, playerMock2 };
    private LobbyMock lobbyMock = new LobbyMock(32, opening, closing, 15, playerMocks);
    private MatchMock matchMock = new MatchMock(21, opening, closing, lobbyMock, livePlayerMocks);
    private ResultMock resultMock = new ResultMock(playerMock1, matchMock,3);

    @Test
    void testSerializationAndDeserialization(){
        JSONObject jsonObject = resultMock.serialize();
        ResultMock deserializedResultMock = JSONSerializable.deserialize(ResultMock.class, jsonObject);
        Assertions.assertEquals(3, deserializedResultMock.getPoints());
        Assertions.assertEquals(playerMock1.getId(), deserializedResultMock.getPlayer().getId());
    }

    @Test
    void getPlayer() {
        Assertions.assertEquals(playerMock1, resultMock.getPlayer());
    }

    @Test
    void getMatch() {
        Assertions.assertEquals(matchMock, resultMock.getMatch());
    }

    @Test
    void getPoints() {
        Assertions.assertEquals(3, resultMock.getPoints());
    }
}