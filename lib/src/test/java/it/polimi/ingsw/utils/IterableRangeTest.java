package it.polimi.ingsw.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class IterableRangeTest {

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

    @Test
    void testFromString() {
        IterableRange<Integer> range = IterableRange.fromString("1..3", "..", Integer::parseInt, IterableRange.INTEGER_INCREMENT_FUNCTION);

        Assertions.assertEquals(1, range.getStart().intValue());
        Assertions.assertEquals(3, range.getEnd().intValue());
    }

    @Test
    void testSingleValued() {
        IterableRange<Integer> range = IterableRange.singleValued(10, IterableRange.INTEGER_INCREMENT_FUNCTION);

        Assertions.assertEquals(10, range.getStart().intValue());
        Assertions.assertEquals(10, range.getEnd().intValue());
    }

    @Test
    void testUnitaryInteger() {
        IterableRange<Integer> range = IterableRange.unitaryInteger();

        Assertions.assertEquals(1, range.getStart().intValue());
        Assertions.assertEquals(1, range.getEnd().intValue());
    }

    @Test
    void testUnitaryFloat() {
        IterableRange<Float> range = IterableRange.unitaryFloat();

        Assertions.assertEquals(1, range.getStart().floatValue());
        Assertions.assertEquals(1, range.getEnd().floatValue());
    }

    @Test
    void testUnitaryDouble() {
        IterableRange<Double> range = IterableRange.unitaryDouble();

        Assertions.assertEquals(1, range.getStart().doubleValue());
        Assertions.assertEquals(1, range.getEnd().doubleValue());
    }

    @Test
    void testUnitaryByte() {
        IterableRange<Byte> range = IterableRange.unitaryByte();

        Assertions.assertEquals(1, range.getStart().byteValue());
        Assertions.assertEquals(1, range.getEnd().byteValue());
    }

    @Test
    void testUnitaryLong() {
        IterableRange<Long> range = IterableRange.unitaryLong();

        Assertions.assertEquals(1, range.getStart().longValue());
        Assertions.assertEquals(1, range.getEnd().longValue());
    }

    @Test
    void testNoSuchElementExceptionOfIterator() {
        IterableRange<Long> range = IterableRange.unitaryLong();
        Iterator<Long> iterator = range.iterator();

        iterator.next();

        Assertions.assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testForEach() {
        IterableRange<Integer> range = new IterableRange<>(3, 16, IterableRange.INTEGER_INCREMENT_FUNCTION);

        Integer[] rangeArray = new Integer[] { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

        AtomicInteger index = new AtomicInteger(0);

        range.forEach(integer -> {
            Assertions.assertEquals(rangeArray[index.get()], integer);

            index.getAndIncrement();
        });
    }

    @Test
    void testStream() {
        IterableRange<Integer> range = new IterableRange<>(3, 16, IterableRange.INTEGER_INCREMENT_FUNCTION);

        List<Integer> integerList = range.stream()
                .map(integer -> ++integer)
                .collect(Collectors.toList());

        Integer[] rangeArray = new Integer[] { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

        for (int i = 0; i < rangeArray.length; i++) {
            Assertions.assertEquals(rangeArray[i] + 1, integerList.get(i).intValue());
        }
    }

    @Test
    void testParallelStream() {
        IterableRange<Integer> range = new IterableRange<>(3, 16, IterableRange.INTEGER_INCREMENT_FUNCTION);

        List<Integer> integerList = range.parallelStream()
                .map(integer -> ++integer)
                .collect(Collectors.toList());

        Integer[] rangeArray = new Integer[] { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

        for (int i = 0; i < rangeArray.length; i++) {
            Assertions.assertEquals(rangeArray[i] + 1, integerList.get(i).intValue());
        }
    }

    @Test
    @SuppressWarnings({"SimplifiableJUnitAssertion", "ConstantConditions", "ObjectEqualsNull", "EqualsWithItself", "EqualsBetweenInconvertibleTypes"})
    void testEquals() {
        IterableRange<Integer> range = new IterableRange<>(3, 16, IterableRange.INTEGER_INCREMENT_FUNCTION);

        Assertions.assertFalse(range.equals(null));
        Assertions.assertTrue(range.equals(range));
        Assertions.assertFalse(range.equals(3));
        Assertions.assertFalse(range.equals(IterableRange.unitaryFloat()));
        Assertions.assertFalse(range.equals(IterableRange.unitaryInteger()));
        Assertions.assertFalse(range.equals(IterableRange.singleValued(4, IterableRange.INTEGER_INCREMENT_FUNCTION)));
    }

    @Test
    void testHashCode() {
        IterableRange<Integer> range = new IterableRange<>(3, 16, IterableRange.INTEGER_INCREMENT_FUNCTION);

        Assertions.assertEquals(Objects.hash(range.getStart(), range.getEnd(), range.incrementFunction), range.hashCode());
    }
}