package it.polimi.ingsw.server.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.server.instructions.predicates.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TakeIfInstructionTest {

    @Test
    void testTruePredicate() {
        Predicate predicate = mock(Predicate.class);
        when(predicate.evaluate(any(Context.class))).thenReturn(true);

        Map<String, Predicate> predicateMap = new HashMap<>();
        predicateMap.put("predicate", predicate);

        TakeIfInstruction instruction = new TakeIfInstruction(predicateMap);
        Integer result = instruction.run(Context.getSharedInstance());

        Assertions.assertEquals(1, result.intValue());
    }

    @Test
    void testFalsePredicate() {
        Predicate predicate = mock(Predicate.class);
        when(predicate.evaluate(any(Context.class))).thenReturn(false);

        Map<String, Predicate> predicateMap = new HashMap<>();
        predicateMap.put("predicate", predicate);

        TakeIfInstruction instruction = new TakeIfInstruction(predicateMap);
        Integer result = instruction.run(Context.getSharedInstance());

        Assertions.assertEquals(0, result.intValue());
    }
}