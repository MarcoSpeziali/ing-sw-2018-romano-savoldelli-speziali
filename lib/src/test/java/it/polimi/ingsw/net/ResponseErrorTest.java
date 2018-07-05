package it.polimi.ingsw.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResponseErrorTest {

    ResponseError responseError = new ResponseError(404, "non found");


    @Test
    void getErrorCode() {
        Assertions.assertEquals(404, responseError.getErrorCode());
    }

    @Test
    void getReason() {
        Assertions.assertEquals("not found", responseError.getReason());

    }
}