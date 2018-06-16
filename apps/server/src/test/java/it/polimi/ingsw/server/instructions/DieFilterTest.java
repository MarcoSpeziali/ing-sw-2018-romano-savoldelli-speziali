package it.polimi.ingsw.server.instructions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class DieFilterTest {

    private static Stream<Arguments> testFromStringArguments() {
        return Stream.of(
                Arguments.of("color", DieFilter.COLOR),
                Arguments.of("shade", DieFilter.SHADE)
        );
    }

    @ParameterizedTest
    @MethodSource("testFromStringArguments")
    void testFromString(String value, DieFilter expectedFilter) {
        Assertions.assertEquals(expectedFilter, DieFilter.fromString(value));
    }

    @Test
    void testFromStringException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> DieFilter.fromString("test"));
    }
}