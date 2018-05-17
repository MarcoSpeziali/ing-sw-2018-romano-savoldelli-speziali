package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.Window;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CountDiagonalInstructionTest {

    private Context context;
    private Die[][] dice;

    @BeforeEach
    void setUp() {
        Window window = mock(Window.class);
        when(window.getCells()).then(invocationOnMock -> cellsFromDice(this.dice));

        this.context = mock(Context.class);
        when(context.get(Context.WINDOW)).thenReturn(window);

        /*
         * [R:1][G:4][R:1][Y:2][P:4]
         * [G:5][R:1][G:2][R:2][Y:1]
         * [R:5][G:5][R:2][Y:3][R:4]
         * [G:5][R:5][Y:3][R:4][T:3]
         */
        this.dice = new Die[][] {
                {
                        new Die(GlassColor.RED, 1),
                        new Die(GlassColor.GREEN, 4),
                        new Die(GlassColor.RED, 1),
                        new Die(GlassColor.YELLOW, 2),
                        new Die(GlassColor.MAGENTA, 4)
                },
                {
                        new Die(GlassColor.GREEN, 5),
                        new Die(GlassColor.RED, 1),
                        new Die(GlassColor.GREEN, 2),
                        new Die(GlassColor.RED, 2),
                        new Die(GlassColor.YELLOW, 1)
                },
                {
                        new Die(GlassColor.RED, 5),
                        new Die(GlassColor.GREEN, 5),
                        new Die(GlassColor.RED, 2),
                        new Die(GlassColor.YELLOW, 3),
                        new Die(GlassColor.RED, 4)
                },
                {
                        new Die(GlassColor.GREEN, 5),
                        new Die(GlassColor.RED, 5),
                        new Die(GlassColor.YELLOW, 3),
                        new Die(GlassColor.RED, 4),
                        new Die(GlassColor.YELLOW, 3)
                }
        };
    }

    private static Cell[][] cellsFromDice(Die[][] dice) {
        Cell[][] cells = new Cell[dice.length][dice[0].length];

        for (int i = 0; i < dice.length; i++) {
            for (int j = 0; j < dice[i].length; j++) {
                cells[i][j] = new Cell(0, null);
                cells[i][j].putDie(dice[i][j]);
            }
        }

        return cells;
    }

    @ParameterizedTest
    @MethodSource("testStrictColorArguments")
    void testStrictColor(GlassColor strictColor, Integer expectedResult) {
        CountDiagonalInstruction instruction = new CountDiagonalInstruction(DieFilter.COLOR, 0, strictColor);
        Assertions.assertEquals(expectedResult, instruction.run(this.context));
    }

    @ParameterizedTest
    @MethodSource("testStrictShadeArguments")
    void testStrictShade(Integer strictShade, Integer expectedResult) {
        CountDiagonalInstruction instruction = new CountDiagonalInstruction(DieFilter.SHADE, strictShade, null);
        Assertions.assertEquals(expectedResult, instruction.run(this.context));
    }

    @Test
    void testGenericColor() {
        CountDiagonalInstruction instruction = new CountDiagonalInstruction(DieFilter.COLOR, 0, null);
        Assertions.assertEquals(19, instruction.run(this.context).intValue());
    }

    @Test
    void testGenericShade() {
        CountDiagonalInstruction instruction = new CountDiagonalInstruction(DieFilter.SHADE, 0, null);
        Assertions.assertEquals(17, instruction.run(this.context).intValue());
    }

    private static Stream<Arguments> testStrictColorArguments() {
        return Stream.of(
                Arguments.of(GlassColor.RED, 9),
                Arguments.of(GlassColor.GREEN, 5),
                Arguments.of(GlassColor.BLUE, 0),
                Arguments.of(GlassColor.YELLOW, 5),
                Arguments.of(GlassColor.MAGENTA, 0)
        );
    }

    private static Stream<Arguments> testStrictShadeArguments() {
        return Stream.of(
                Arguments.of(1, 3),
                Arguments.of(2, 4),
                Arguments.of(3, 3),
                Arguments.of(4, 2),
                Arguments.of(5, 5),
                Arguments.of(6, 0)
        );
    }
}