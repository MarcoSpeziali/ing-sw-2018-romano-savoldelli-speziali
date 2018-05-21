package it.polimi.ingsw.utils.streams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static it.polimi.ingsw.utils.streams.StreamUtils.not;
import static org.junit.jupiter.api.Assertions.*;

class StreamUtilsTest {

    @Test
    void testNot() {
        Predicate<Integer> originalPredicate = integer -> integer % 2 == 0;
        Assertions.assertFalse(originalPredicate.test(5));

        Predicate<Integer> invertedPredicate = not(originalPredicate);
        Assertions.assertTrue(invertedPredicate.test(5));
    }
}