package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CellMockTest {
    private DieMock dieMock = new DieMock(3, GlassColor.PURPLE, 0);
    private CellMock cellMock = new CellMock(0, GlassColor.PURPLE, dieMock, 0);

    @Test
    void testSerializationAndDeserialization(){
        JSONObject jsonObject = cellMock.serialize();
        CellMock deserializedCellMock = JSONSerializable.deserialize(CellMock.class, jsonObject);
        Assertions.assertEquals(GlassColor.PURPLE, deserializedCellMock.getDie().getColor());
    }

    @Test
    void getColor() {
        Assertions.assertEquals(GlassColor.PURPLE, cellMock.getColor());
    }

    @Test
    void getShade() {
        int shade = cellMock.getShade();
        Assertions.assertEquals(0, shade);
    }

    @Test
    void getDie() {
        Assertions.assertEquals(dieMock, cellMock.getDie());
    }
}