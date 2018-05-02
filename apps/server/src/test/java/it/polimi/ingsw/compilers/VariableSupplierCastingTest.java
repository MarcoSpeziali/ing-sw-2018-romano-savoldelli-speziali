package it.polimi.ingsw.compilers;

import it.polimi.ingsw.compilers.utils.VariableSupplierCasting;
import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class VariableSupplierCastingTest {

    @Test
    void testNullVariable() {
        Assertions.assertNull(VariableSupplierCasting.cast("null"));
    }

    @Test
    void testIntegerVariable() {
        Object casted = VariableSupplierCasting.cast("12");

        Assertions.assertNotNull(casted);
        Assertions.assertEquals(Integer.class, casted.getClass());
        Assertions.assertEquals(12, casted);
    }

    @Test
    void testFloatVariable1() {
        Object casted = VariableSupplierCasting.cast("12.0");

        Assertions.assertNotNull(casted);
        Assertions.assertEquals(Double.class, casted.getClass());
        Assertions.assertEquals(12.0D, casted);
    }

    @Test
    void testFloatVariable2() {
        Object casted = VariableSupplierCasting.cast(".04");

        Assertions.assertNotNull(casted);
        Assertions.assertEquals(Double.class, casted.getClass());
        Assertions.assertEquals(.04D, casted);
    }

    @Test
    void testFloatVariable3() {
        Object casted = VariableSupplierCasting.cast("0.04");

        Assertions.assertNotNull(casted);
        Assertions.assertEquals(Double.class, casted.getClass());
        Assertions.assertEquals(0.04D, casted);
    }

    @ParameterizedTest
    @MethodSource("stringColorAndExpectedEnumProvider")
    void testStringVariable(String colorString, GlassColor expectedColor) {
        Object casted = VariableSupplierCasting.cast(colorString);

        Assertions.assertNotNull(casted);
        Assertions.assertEquals(GlassColor.class, casted.getClass());
        Assertions.assertEquals(expectedColor, casted);
    }

    private static Stream<Arguments> stringColorAndExpectedEnumProvider() {
        return Stream.of(
                Arguments.of("red", GlassColor.RED),
                Arguments.of("yellow", GlassColor.YELLOW),
                Arguments.of("green", GlassColor.GREEN),
                Arguments.of("blue", GlassColor.BLUE),
                Arguments.of("purple", GlassColor.PURPLE)
        );
    }
}