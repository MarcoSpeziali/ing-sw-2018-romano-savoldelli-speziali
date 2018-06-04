package it.polimi.ingsw.net;

import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.net.utils.RequestFields;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BodyTest {

    private Body body;

    @BeforeEach
    public void setUp() {
        this.body = new Body(EndPointFunction.SIGN_UP, Map.of(
                RequestFields.Authentication.USERNAME.getFieldName(), "username-test",
                RequestFields.Authentication.PASSWORD.getFieldName(), "password-test"
        ));
    }

    @Test
    void testGetEndPointFunction() {
        Assertions.assertEquals(EndPointFunction.SIGN_UP, this.body.getEndPointFunction());
    }

    @Test
    void testSetEndPointFunction() {
        Assertions.assertEquals(EndPointFunction.SIGN_UP, this.body.getEndPointFunction());

        this.body.setEndPointFunction(EndPointFunction.FULFILL_AUTHENTICATION_CHALLENGE);

        Assertions.assertEquals(EndPointFunction.FULFILL_AUTHENTICATION_CHALLENGE, this.body.getEndPointFunction());
    }

    @Test
    void testNullEndPointFunction() {
        this.body.setEndPointFunction(null);

        Assertions.assertNull(this.body.getEndPointFunction());
    }

    @Test
    void testDeserialize() {
        JSONObject jsonObject = new JSONObject(
                "{ \"endpoint\": \"sign-up\", \"username\": \"username-test\", \"password\": \"password-test\" }"
        );

        Body body = new Body();
        body.deserialize(jsonObject);

        Assertions.assertEquals(EndPointFunction.SIGN_UP, body.getEndPointFunction());
        Assertions.assertEquals("username-test", this.body.get("username"));
        Assertions.assertEquals("password-test", this.body.get("password"));
    }

    @Test
    void testSerialize() {
        JSONObject jsonObject = this.body.serialize();

        Assertions.assertNotNull(jsonObject);

        Assertions.assertEquals(EndPointFunction.SIGN_UP.getEndPointFunctionName(), jsonObject.getString("endpoint"));
        Assertions.assertEquals("username-test", jsonObject.getString("username"));
        Assertions.assertEquals("password-test", jsonObject.getString("password"));
    }
}