package it.polimi.ingsw.server.compilers.instructions.directives;

import it.polimi.ingsw.server.instructions.Instruction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class InstructionDirectiveTest {

    @Test
    void testGetters() {
        List<InstructionParameterDirective> instructionParameterDirectiveList = List.of();
        List<InstructionExposedVariableDirective> instructionExposedVariableDirectives = List.of();

        InstructionDirective instructionDirective = new InstructionDirective(
                "id",
                Instruction.class,
                instructionParameterDirectiveList,
                instructionExposedVariableDirectives
        );

        Assertions.assertEquals("id", instructionDirective.getId());
        Assertions.assertEquals(Instruction.class, instructionDirective.getTargetClass());
        Assertions.assertSame(instructionParameterDirectiveList, instructionDirective.getParameterDirectives());
        Assertions.assertSame(instructionExposedVariableDirectives, instructionDirective.getExposedVariableDirectives());
    }
}