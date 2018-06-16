package it.polimi.ingsw.server.compilers.instructions.directives;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

class InstructionExposedVariableDirectiveTest {

    @Test
    void testGetters() {
        InstructionExposedVariableDirective instructionExposedVariableDirective = new InstructionExposedVariableDirective(
                "name",
                Serializable.class
        );

        Assertions.assertEquals("name", instructionExposedVariableDirective.getName());
        Assertions.assertEquals(Serializable.class, instructionExposedVariableDirective.getVariableType());
    }
}