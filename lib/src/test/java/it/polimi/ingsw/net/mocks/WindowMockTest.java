package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WindowMockTest {
    private DieMock dieMock = new DieMock(3, GlassColor.PURPLE, 0);
    private CellMock cellMock1 = new CellMock(0,GlassColor.PURPLE, dieMock);
    private CellMock cellMock2 = new CellMock(3, GlassColor.RED, null);
    private CellMock[] cellMocks = new CellMock[]{cellMock1, cellMock2};
    private WindowMock windowMock = new WindowMock("prova", 3, 1, 2, cellMocks);

    @Test
    void testSerializationAndDeserialization() {
        JSONObject jsonObject = windowMock.serialize();
        WindowMock deserializedWindowMock = JSONSerializable.deserialize(WindowMock.class, jsonObject);
        Assertions.assertEquals(GlassColor.PURPLE, deserializedWindowMock.getFlatCells()[0].getDie().getColor());
    }

    @Test
    void getDifficulty() {
        Assertions.assertEquals(3, windowMock.getDifficulty());
    }

    @Test
    void getId() {
        Assertions.assertEquals("prova", windowMock.getId());
    }

    @Test
    void getRows() {
        Assertions.assertEquals(1, windowMock.getRows());
    }

    @Test
    void getColumns() {
        Assertions.assertEquals(2, windowMock.getColumns());
    }

    @Test
    void getFlatCells() {
        Assertions.assertEquals(GlassColor.RED, windowMock.getFlatCells()[1].getColor());
    }
}