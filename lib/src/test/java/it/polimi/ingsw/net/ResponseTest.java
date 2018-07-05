package it.polimi.ingsw.net;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.net.mocks.DieMock;
import it.polimi.ingsw.net.utils.ResponseFields;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResponseTest {

    ResponseError responseError = new ResponseError(404, "not found");
    JSONSerializable body = new DieMock(5, GlassColor.PURPLE);
    Header header = new Header("test");
    Response response = new Response(header, body, responseError );

    @Test
    void getHeader() {
        Assertions.assertEquals(header, response.getHeader());
    }

    @Test
    void getBody() {
        Assertions.assertEquals(body, response.getBody());

    }

    @Test
    void getError() {
        Assertions.assertEquals(responseError, response.getError());

    }

    @Test
    void serialize() {
        JSONObject jsonObject = response.serialize();

        jsonObject
                .getJSONObject(ResponseFields.RESPONSE.toString())
                .getJSONObject(ResponseFields.BODY.toString())
                .put(ResponseFields.Body.CLASS_TYPE.toString(), this.body.getClass().getName());

        Assertions.assertEquals(jsonObject, response.serialize());

    }
}