package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TakeMinInstructionTest {

    @ParameterizedTest
    @MethodSource("testRunArguments")
    void testRun(List<Instruction> instructionsList, Integer expectedResult) {
        TakeMinInstruction instruction = new TakeMinInstruction();
        //noinspection unchecked
        instruction.instructions = mock(List.class);

        when(instruction.instructions.isEmpty()).thenReturn(instructionsList.isEmpty());
        when(instruction.instructions.size()).thenReturn(instructionsList.size());
        when(instruction.instructions.stream()).thenReturn(instructionsList.stream());

        Integer result = instruction.run(null);
        Assertions.assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> testRunArguments() {
        Instruction instruction1 = mock(Instruction.class);
        when(instruction1.run(any(Context.class))).thenReturn(5);

        Instruction instruction2 = mock(Instruction.class);
        when(instruction2.run(any(Context.class))).thenReturn(9);

        Instruction instruction3 = mock(Instruction.class);
        when(instruction3.run(any(Context.class))).thenReturn(12);

        Instruction instruction4 = mock(Instruction.class);
        when(instruction4.run(any(Context.class))).thenReturn(13);

        Instruction instruction5 = mock(Instruction.class);
        when(instruction5.run(any(Context.class))).thenReturn(2);

        return Stream.of(
                Arguments.of(List.of(instruction1, instruction2, instruction3, instruction4, instruction5), 2),
                Arguments.of(List.of(), Integer.MAX_VALUE),
                Arguments.of(List.of(instruction1, instruction2, instruction4), 5),
                Arguments.of(List.of(instruction5), 2)
        );
    }
}