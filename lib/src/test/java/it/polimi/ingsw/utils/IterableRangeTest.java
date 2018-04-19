package it.polimi.ingsw.utils;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class IterableRangeTest {

    @Test
    void testIterable() {
        IterableRange<Float> range = new IterableRange<>(12.6F, 22.99F, (val) -> ++val);

        Float iteratorVal = range.getStart();
        for (Float val : range) {
            Assertions.assertEquals(iteratorVal, val);

            iteratorVal++;
        }

        Assertions.assertEquals(23.6f, iteratorVal.floatValue());
    }

    @Test
    void testIterableSameValue() {
        IterableRange<Integer> range = new IterableRange<>(1, 1, (val) -> ++val);

        Integer iteratorVal = range.getStart();
        for (Integer val : range) {
            Assertions.assertEquals(iteratorVal, val);

            iteratorVal++;
        }

        Assertions.assertEquals(2, iteratorVal.intValue());
    }
}