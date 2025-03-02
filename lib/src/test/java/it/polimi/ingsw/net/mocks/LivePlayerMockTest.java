package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.core.GlassColor;

class LivePlayerMockTest {
    private DieMock dieMock = new DieMock(3, GlassColor.PURPLE);
    private CellMock cellMock1 = new CellMock(0,GlassColor.PURPLE, dieMock);
    private CellMock cellMock2 = new CellMock(3, GlassColor.RED, null);
    private CellMock[] cellMocks = new CellMock[]{cellMock1, cellMock2};
    private WindowMock windowMock = new WindowMock("prova", 3, 1, 2, cellMocks);
    private PlayerMock playerMock = new PlayerMock(1,"prova");

    // private LivePlayerMock livePlayerMock = new LivePlayerMock(1, windowMock, playerMock);

    /*
    @Test
    void testSerializationAndDeserialization() {
        JSONObject jsonObject = windowMock.serialize();
        LivePlayerMock deserializedLivePlayerMock = JSONSerializable.deserialize(LivePlayerMock.class, jsonObject);
    }

    @Test
    void getFavourTokens() {
        Assertions.assertEquals(1, livePlayerMock.getFavourTokens());
    }

    @Test
    void getWindow() {
        Assertions.assertEquals("prova", livePlayerMock.getWindow().getId());
    }

    @Test
    void getPlayer() {
        Assertions.assertEquals("prova", livePlayerMock.getPlayer().getUsername());
    }
    */
}