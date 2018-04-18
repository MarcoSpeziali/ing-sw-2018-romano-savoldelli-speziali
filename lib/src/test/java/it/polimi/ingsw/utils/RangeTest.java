package it.polimi.ingsw.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RangeTest {

    @Test
    void testRangeCreation() {
        new Range<>(15, 30);
        new Range<>(1, 1);
    }

    @Test
    void testInvalidRange() throws IllegalArgumentException {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Range<>(30, 15);
        });
    }

    @Test
    void testGetters() {
        Range<Integer> range = new Range<>(15, 30);

        Assertions.assertEquals(15, range.getStart().intValue());
        Assertions.assertEquals(30, range.getEnd().intValue());
    }

    @Test
    void testContainsValue() {
        Range<Integer> range = new Range<>(15, 30);

        Assertions.assertTrue(range.containsValue(20));
        Assertions.assertTrue(range.containsValue(15));
        Assertions.assertTrue(range.containsValue(30));

        Assertions.assertFalse(range.containsValue(40));
    }

    @Test
    void testIsInsideRange() {
        Range<Integer> range = new Range<>(15, 30);
        Range<Integer> range1 = new Range<>(12, 33);
        Range<Integer> range2 = new Range<>(12, 18);
        Range<Integer> range3 = new Range<>(17, 40);
        Range<Integer> range4 = new Range<>(0, 12);

        Assertions.assertTrue(range.isInsideRange(range1));

        Assertions.assertFalse(range.isInsideRange(range2));
        Assertions.assertFalse(range.isInsideRange(range3));
        Assertions.assertFalse(range.isInsideRange(range4));
    }

    @Test
    void testContainsRange() {
        Range<Integer> range = new Range<>(15, 30);
        Range<Integer> range1 = new Range<>(12, 33);
        Range<Integer> range2 = new Range<>(12, 18);
        Range<Integer> range3 = new Range<>(17, 40);
        Range<Integer> range4 = new Range<>(0, 12);
        Range<Integer> range5 = new Range<>(17, 28);

        Assertions.assertFalse(range.containsRange(range1));
        Assertions.assertFalse(range.containsRange(range2));
        Assertions.assertFalse(range.containsRange(range3));
        Assertions.assertFalse(range.containsRange(range4));

        Assertions.assertTrue(range.containsRange(range5));
    }

    @Test
    void testFromString() {
        String rangeRep = "2..6";
        Range<Integer> range = Range.fromString(rangeRep, "..", Integer::parseInt);

        Assertions.assertEquals(2, range.getStart().intValue());
        Assertions.assertEquals(6, range.getEnd().intValue());

        rangeRep = "2";
        range = Range.fromString(rangeRep, "..", Integer::parseInt);

        Assertions.assertEquals(2, range.getStart().intValue());
        Assertions.assertEquals(2, range.getEnd().intValue());

        rangeRep = "";
        range = Range.fromString(rangeRep, "..", Integer::parseInt);

        Assertions.assertNull(range);
    }

    @Test
    void testToString() {
        Range<Integer> range = new Range<>(15, 30);

        Assertions.assertEquals("[15 - 30]", range.toString());
    }
}