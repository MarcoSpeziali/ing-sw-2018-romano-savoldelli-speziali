package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class TakeShadeInstructionTest {

    @ParameterizedTest
    @MethodSource("testRunArguments")
    void testRun(Die die) {
        Assertions.assertEquals(
                die.getShade(),
                new TakeShadeInstruction(context -> die)
                        .run(Context.getSharedInstance())
        );
    }

    private static Stream<Arguments> testRunArguments() {
        return Stream.of(
                Arguments.of(new Die(GlassColor.RED, 1)),
                Arguments.of(new Die(GlassColor.YELLOW, 2)),
                Arguments.of(new Die(GlassColor.RED, 3)),
                Arguments.of(new Die(GlassColor.BLUE, 4)),
                Arguments.of(new Die(GlassColor.MAGENTA, 5)),
                Arguments.of(new Die(GlassColor.GREEN, 6))
        );
    }
}