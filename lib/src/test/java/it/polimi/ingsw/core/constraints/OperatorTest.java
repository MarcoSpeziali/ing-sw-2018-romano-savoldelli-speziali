package it.polimi.ingsw.core.constraints;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class OperatorTest {

    @ParameterizedTest
    @MethodSource("testFromStringArguments")
    void testFromString(String from, Operator expectedOperator) {
        Assertions.assertEquals(expectedOperator, Operator.fromString(from));
    }

    @ParameterizedTest
    @MethodSource("testToStringArguments")
    void testToString(Operator operator, String expectedString) {
        Assertions.assertEquals(expectedString, operator.toString());
    }

    @Test
    void testFromStringThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            //noinspection ResultOfMethodCallIgnored
            Operator.fromString("test val");
        });
    }

    private static Stream<Arguments> testFromStringArguments() {
        return Stream.of(
                Arguments.of("==", Operator.EQUALS),
                Arguments.of("!=", Operator.NOT_EQUALS),
                Arguments.of(">", Operator.GREATER),
                Arguments.of(">=", Operator.GREATER_EQUAL),
                Arguments.of("<", Operator.LESS),
                Arguments.of("<=", Operator.LESS_EQUAL)
        );
    }

    private static Stream<Arguments> testToStringArguments() {
        return Stream.of(
                Arguments.of(Operator.EQUALS, "=="),
                Arguments.of(Operator.NOT_EQUALS, "!="),
                Arguments.of(Operator.GREATER, ">"),
                Arguments.of(Operator.GREATER_EQUAL, ">="),
                Arguments.of(Operator.LESS, "<"),
                Arguments.of(Operator.LESS_EQUAL, "<=")
        );
    }
}