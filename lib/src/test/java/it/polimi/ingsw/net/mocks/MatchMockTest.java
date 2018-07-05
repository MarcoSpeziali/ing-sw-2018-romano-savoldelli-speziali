package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.requests.ChoosePositionForLocationRequest;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class MatchMockTest {

    @Test
    void testSerializationAndDeserialization() {
        JSONObject jsonObject = new JSONObject("{\"request\":{\"header\":{\"endpoint\":\"MATCH_PLAYER_TOOL_CARD_CHOOSE_POSITION_REQUEST\"},\"body\":{\"match-id\":234,\"location\":{\"class\":\"it.polimi.ingsw.net.mocks.DraftPoolMock\",\"location-die-map\":{\"4\":{\"color\":\"GREEN\",\"shade\":5},\"3\":{\"color\":\"GREEN\",\"shade\":3}},\"max-number-of-dice\":5},\"unavailable-locations\":[0,1,2],\"class-type\":\"it.polimi.ingsw.net.requests.ChoosePositionForLocationRequest\"}}}");
        Request<ChoosePositionForLocationRequest> choosePositionForLocationRequest = JSONSerializable.deserialize(Request.class, jsonObject);
    }
}