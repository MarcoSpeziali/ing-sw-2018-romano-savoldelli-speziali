package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

class InstructionTest {

    private Instruction instruction;

    @BeforeEach
    void setUp() {
        this.instruction = mock(Instruction.class, CALLS_REAL_METHODS);
        this.instruction.setInstructions(List.of());
    }

    @Test
    void testGetInstructions() {
        Assertions.assertNotNull(this.instruction.getInstructions());
        Assertions.assertEquals(0, this.instruction.getInstructions().size());
    }

    @Test
    void testNullRun() {
        this.instruction.setInstructions(null);
        Assertions.assertEquals(0, this.instruction.run(Context.getSharedInstance()).intValue());
    }

    @Test
    void testEmptyRun() {
        Assertions.assertEquals(0, this.instruction.run(Context.getSharedInstance()).intValue());
    }
}