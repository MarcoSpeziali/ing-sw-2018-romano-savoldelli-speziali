package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.instructions.predicates.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TakeIfInstructionTest {

    @Test
    void testTruePredicate() {
        Predicate predicate = mock(Predicate.class);
        when(predicate.evaluate(any(Context.class))).thenReturn(true);

        TakeIfInstruction instruction = new TakeIfInstruction(predicate);
        Integer result = instruction.run(Context.getSharedInstance());

        Assertions.assertEquals(1, result.intValue());
    }

    @Test
    void testFalsePredicate() {
        Predicate predicate = mock(Predicate.class);
        when(predicate.evaluate(any(Context.class))).thenReturn(false);

        TakeIfInstruction instruction = new TakeIfInstruction(predicate);
        Integer result = instruction.run(Context.getSharedInstance());

        Assertions.assertEquals(0, result.intValue());
    }
}