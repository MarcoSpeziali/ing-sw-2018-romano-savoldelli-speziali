package it.polimi.ingsw.server.compilers.instructions.directives;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

class InstructionParameterDirectiveTest {

    @Test
    void testGetters() {
        InstructionParameterDirective instructionParameterDirective = new InstructionParameterDirective(
                Serializable.class,
                2,
                false,
                false,
                false
        );

        Assertions.assertFalse(instructionParameterDirective.isPredicate());
        Assertions.assertFalse(instructionParameterDirective.isOptional());
    }
}