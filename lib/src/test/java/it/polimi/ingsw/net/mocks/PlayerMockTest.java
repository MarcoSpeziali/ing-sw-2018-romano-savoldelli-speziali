package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONSerializable;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.utils.io.json.JSONSerializable.deserialize;

class PlayerMockTest {

    private PlayerMock playerMock = new PlayerMock(1,"prova");

    @Test
    void testSerializationAndDeserialization(){
        JSONObject jsonObject = playerMock.serialize();
        PlayerMock deserializedPlayerMock = JSONSerializable.deserialize(PlayerMock.class, jsonObject);
        Assertions.assertEquals(playerMock.getId(), deserializedPlayerMock.getId());
        Assertions.assertEquals(playerMock.getUsername(), deserializedPlayerMock.getUsername());

    }

    @Test
    void getId() {
        Assertions.assertEquals(1, playerMock.getId());
    }

    @Test
    void getUsername() {
        Assertions.assertEquals("prova", playerMock.getUsername());
    }
}