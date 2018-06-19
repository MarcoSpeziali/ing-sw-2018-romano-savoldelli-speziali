package it.polimi.ingsw.server.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.Window;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CountDiceInstructionTest {

    private static Stream<Arguments> testColorFilterArguments() {
        return Stream.of(
                Arguments.of(GlassColor.RED, 9),
                Arguments.of(GlassColor.GREEN, 5),
                Arguments.of(GlassColor.BLUE, 0),
                Arguments.of(GlassColor.YELLOW, 5),
                Arguments.of(GlassColor.PURPLE, 1)
        );
    }

    private static Stream<Arguments> testShadeFilterArguments() {
        return Stream.of(
                Arguments.of(1, 4),
                Arguments.of(2, 4),
                Arguments.of(3, 3),
                Arguments.of(4, 4),
                Arguments.of(5, 5),
                Arguments.of(6, 0)
        );
    }

    @BeforeEach
    void setUp() {
        Window window = mock(Window.class);
        when(window.getDice()).thenReturn(
                List.of(
                        new Die(1, GlassColor.RED),
                        new Die(4, GlassColor.GREEN),
                        new Die(1, GlassColor.RED),
                        new Die(2, GlassColor.YELLOW),
                        new Die(4, GlassColor.PURPLE),

                        new Die(5, GlassColor.GREEN),
                        new Die(1, GlassColor.RED),
                        new Die(2, GlassColor.GREEN),
                        new Die(2, GlassColor.RED),
                        new Die(1, GlassColor.YELLOW),

                        new Die(5, GlassColor.RED),
                        new Die(5, GlassColor.GREEN),
                        new Die(2, GlassColor.RED),
                        new Die(3, GlassColor.YELLOW),
                        new Die(4, GlassColor.RED),

                        new Die(5, GlassColor.GREEN),
                        new Die(5, GlassColor.RED),
                        new Die(3, GlassColor.YELLOW),
                        new Die(4, GlassColor.RED),
                        new Die(3, GlassColor.YELLOW)
                )
        );
        Context.getSharedInstance().put(Context.WINDOW, window);
    }

    @Test
    void testNotFilter() {
        CountDiceInstruction instruction = new CountDiceInstruction(0, null);
        Assertions.assertEquals(20, instruction.run(Context.getSharedInstance()).intValue());
    }

    @ParameterizedTest
    @MethodSource("testShadeFilterArguments")
    void testShadeFilter(Integer shadeFilter, Integer expectedValue) {
        CountDiceInstruction instruction = new CountDiceInstruction(shadeFilter, null);
        Assertions.assertEquals(expectedValue, instruction.run(Context.getSharedInstance()));
    }

    @ParameterizedTest
    @MethodSource("testColorFilterArguments")
    void testColorFilter(GlassColor colorFilter, Integer expectedValue) {
        CountDiceInstruction instruction = new CountDiceInstruction(0, colorFilter);
        Assertions.assertEquals(expectedValue, instruction.run(Context.getSharedInstance()));
    }
}