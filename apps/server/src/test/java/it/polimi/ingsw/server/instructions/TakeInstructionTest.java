package it.polimi.ingsw.server.instructions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TakeInstructionTest {

    @Test
    void run() {
        Assertions.assertEquals(1, new TakeInstruction().run(null).intValue());
    }
}