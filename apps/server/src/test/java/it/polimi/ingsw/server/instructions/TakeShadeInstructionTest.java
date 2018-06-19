package it.polimi.ingsw.server.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class TakeShadeInstructionTest {

    private static Stream<Arguments> testRunArguments() {
        return Stream.of(
                Arguments.of(new Die(1, GlassColor.RED)),
                Arguments.of(new Die(2, GlassColor.YELLOW)),
                Arguments.of(new Die(3, GlassColor.RED)),
                Arguments.of(new Die(4, GlassColor.BLUE)),
                Arguments.of(new Die(5, GlassColor.PURPLE)),
                Arguments.of(new Die(6, GlassColor.GREEN))
        );
    }

    @ParameterizedTest
    @MethodSource("testRunArguments")
    void testRun(Die die) {
        Assertions.assertEquals(
                die.getShade(),
                new TakeShadeInstruction(context -> die)
                        .run(Context.getSharedInstance())
        );
    }
}