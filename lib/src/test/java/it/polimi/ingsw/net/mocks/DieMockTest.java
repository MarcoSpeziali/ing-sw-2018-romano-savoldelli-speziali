package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DieMockTest {
    DieMock die = new DieMock(3, GlassColor.PURPLE, 0);

    @Test
    void testSerializationAndDeserialization(){
        JSONObject jsonObject = die.serialize();
        DieMock deserializedDie = JSONSerializable.deserialize(DieMock.class, jsonObject);
        int shade = deserializedDie.getShade();
        Assertions.assertEquals(3, shade);
    }

    @Test
    void getColor() {
        Assertions.assertEquals(GlassColor.PURPLE, die.getColor());
    }

    @Test
    void getShade() {
        int shade = die.getShade();
        Assertions.assertEquals(3, shade);
    }
}