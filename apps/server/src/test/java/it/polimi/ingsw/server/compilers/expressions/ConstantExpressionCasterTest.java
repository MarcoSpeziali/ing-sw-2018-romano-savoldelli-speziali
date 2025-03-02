package it.polimi.ingsw.server.compilers.expressions;

import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class ConstantExpressionCasterTest {

    private static Stream<Arguments> testBooleanArguments() {
        return Stream.of(
                Arguments.of("true", true),
                Arguments.of("TRUE", true),
                Arguments.of("True", true),
                Arguments.of("false", false),
                Arguments.of("FALSE", false),
                Arguments.of("False", false)
        );
    }

    private static Stream<Arguments> testStringVariableArguments() {
        return Stream.of(
                Arguments.of("red", GlassColor.RED),
                Arguments.of("yellow", GlassColor.YELLOW),
                Arguments.of("green", GlassColor.GREEN),
                Arguments.of("blue", GlassColor.BLUE),
                Arguments.of("purple", GlassColor.PURPLE)
        );
    }

    @Test
    void testNullVariable() {
        Assertions.assertNull(ConstantExpressionCaster.cast("null"));
    }

    @Test
    void testIntegerVariable() {
        Object casted = ConstantExpressionCaster.cast("12");

        Assertions.assertNotNull(casted);
        Assertions.assertEquals(Integer.class, casted.getClass());
        Assertions.assertEquals(12, casted);
    }
    
    @Test
    void testNegativeIntegerVariable() {
        Object casted = ConstantExpressionCaster.cast("-12");
        
        Assertions.assertNotNull(casted);
        Assertions.assertEquals(Integer.class, casted.getClass());
        Assertions.assertEquals(-12, casted);
    }
    
    @Test
    void testBinaryIntegerLiteralVariable() {
        Object casted = ConstantExpressionCaster.cast("0b0011001010");
    
        Assertions.assertNotNull(casted);
        Assertions.assertEquals(Integer.class, casted.getClass());
        Assertions.assertEquals(0b0011001010, casted);
    }
    
    @Test
    void testNegativeBinaryIntegerLiteralVariable() {
        Object casted = ConstantExpressionCaster.cast("-0b0011001010");
        
        Assertions.assertNotNull(casted);
        Assertions.assertEquals(Integer.class, casted.getClass());
        Assertions.assertEquals(-0b0011001010, casted);
    }
    
    @Test
    void testHexIntegerLiteralVariable() {
        Object casted = ConstantExpressionCaster.cast("0xACDf9");
        
        Assertions.assertNotNull(casted);
        Assertions.assertEquals(Integer.class, casted.getClass());
        Assertions.assertEquals(0xACDf9, casted);
    }
    
    @Test
    void testNegativeHexIntegerLiteralVariable() {
        Object casted = ConstantExpressionCaster.cast("-0xACDf9");
        
        Assertions.assertNotNull(casted);
        Assertions.assertEquals(Integer.class, casted.getClass());
        Assertions.assertEquals(-0xACDf9, casted);
    }

    @Test
    void testFloatVariable1() {
        Object casted = ConstantExpressionCaster.cast("12.0");

        Assertions.assertNotNull(casted);
        Assertions.assertEquals(Double.class, casted.getClass());
        Assertions.assertEquals(12.0D, casted);
    }

    @Test
    void testFloatVariable2() {
        Object casted = ConstantExpressionCaster.cast(".04");

        Assertions.assertNotNull(casted);
        Assertions.assertEquals(Double.class, casted.getClass());
        Assertions.assertEquals(.04D, casted);
    }

    @Test
    void testFloatVariable3() {
        Object casted = ConstantExpressionCaster.cast("0.04");

        Assertions.assertNotNull(casted);
        Assertions.assertEquals(Double.class, casted.getClass());
        Assertions.assertEquals(0.04D, casted);
    }
    
    @Test
    void testNegativeFloatVariable1() {
        Object casted = ConstantExpressionCaster.cast("-12.0");
        
        Assertions.assertNotNull(casted);
        Assertions.assertEquals(Double.class, casted.getClass());
        Assertions.assertEquals(-12.0D, casted);
    }
    
    @Test
    void testNegativeFloatVariable2() {
        Object casted = ConstantExpressionCaster.cast("-.04");
        
        Assertions.assertNotNull(casted);
        Assertions.assertEquals(Double.class, casted.getClass());
        Assertions.assertEquals(-.04D, casted);
    }
    
    @Test
    void testNegativeFloatVariable3() {
        Object casted = ConstantExpressionCaster.cast("-0.04");
        
        Assertions.assertNotNull(casted);
        Assertions.assertEquals(Double.class, casted.getClass());
        Assertions.assertEquals(-0.04D, casted);
    }

    @Test
    void testString() {
        Object casted = ConstantExpressionCaster.cast("\"test\"");

        Assertions.assertNotNull(casted);
        Assertions.assertEquals(String.class, casted.getClass());
        Assertions.assertEquals("test", casted);
    }

    @ParameterizedTest
    @MethodSource("testBooleanArguments")
    void testBoolean(String booleanString, Boolean expectedResult) {
        Object casted = ConstantExpressionCaster.cast(booleanString);

        Assertions.assertNotNull(casted);
        Assertions.assertEquals(Boolean.class, casted.getClass());
        Assertions.assertEquals(expectedResult, casted);
    }

    @ParameterizedTest
    @MethodSource("testStringVariableArguments")
    void testStringVariable(String colorString, GlassColor expectedColor) {
        Object casted = ConstantExpressionCaster.cast(colorString);

        Assertions.assertNotNull(casted);
        Assertions.assertEquals(GlassColor.class, casted.getClass());
        Assertions.assertEquals(expectedColor, casted);
    }

    @Test
    void testIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ConstantExpressionCaster.cast("test");
        });
    }
}