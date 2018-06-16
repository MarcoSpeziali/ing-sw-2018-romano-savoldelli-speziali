package it.polimi.ingsw.server.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GlobalContextTest {

    @Test
    void testSingleton() {
        Assertions.assertSame(
                GlobalContext.getGlobalContext(),
                GlobalContext.getGlobalContext()
        );
    }

    @Test
    void testGetContextForPlayer() {
        // TODO: add code
    }
}