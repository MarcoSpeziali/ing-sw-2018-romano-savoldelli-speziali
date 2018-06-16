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
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ForDieInstructionTest {

    private Context context;
    private List<Die> dieList;

    private static Stream<Arguments> testColorAndShadeFilterArguments() {
        return Stream.of(
                Arguments.of(1, GlassColor.RED),
                Arguments.of(2, GlassColor.RED),
                Arguments.of(3, GlassColor.RED),
                Arguments.of(4, GlassColor.RED),
                Arguments.of(5, GlassColor.RED),
                Arguments.of(6, GlassColor.RED),

                Arguments.of(1, GlassColor.GREEN),
                Arguments.of(2, GlassColor.GREEN),
                Arguments.of(3, GlassColor.GREEN),
                Arguments.of(4, GlassColor.GREEN),
                Arguments.of(5, GlassColor.GREEN),
                Arguments.of(6, GlassColor.GREEN),

                Arguments.of(1, GlassColor.BLUE),
                Arguments.of(2, GlassColor.BLUE),
                Arguments.of(3, GlassColor.BLUE),
                Arguments.of(4, GlassColor.BLUE),
                Arguments.of(5, GlassColor.BLUE),
                Arguments.of(6, GlassColor.BLUE),

                Arguments.of(1, GlassColor.YELLOW),
                Arguments.of(2, GlassColor.YELLOW),
                Arguments.of(3, GlassColor.YELLOW),
                Arguments.of(4, GlassColor.YELLOW),
                Arguments.of(5, GlassColor.YELLOW),
                Arguments.of(6, GlassColor.YELLOW),

                Arguments.of(1, GlassColor.PURPLE),
                Arguments.of(2, GlassColor.PURPLE),
                Arguments.of(3, GlassColor.PURPLE),
                Arguments.of(4, GlassColor.PURPLE),
                Arguments.of(5, GlassColor.PURPLE),
                Arguments.of(6, GlassColor.PURPLE)
        );
    }

    @BeforeEach
    @SuppressWarnings("Duplicates")
    void setUp() {
        this.context = Context.getSharedInstance();
        this.dieList = List.of(
                new Die(GlassColor.RED, 1),
                new Die(GlassColor.GREEN, 4),
                new Die(GlassColor.RED, 1),
                new Die(GlassColor.YELLOW, 2),
                new Die(GlassColor.PURPLE, 4),

                new Die(GlassColor.GREEN, 5),
                new Die(GlassColor.RED, 1),
                new Die(GlassColor.GREEN, 2),
                new Die(GlassColor.RED, 2),
                new Die(GlassColor.YELLOW, 1),

                new Die(GlassColor.RED, 5),
                new Die(GlassColor.GREEN, 5),
                new Die(GlassColor.RED, 2),
                new Die(GlassColor.YELLOW, 3),
                new Die(GlassColor.RED, 4),

                new Die(GlassColor.GREEN, 5),
                new Die(GlassColor.RED, 5),
                new Die(GlassColor.YELLOW, 3),
                new Die(GlassColor.RED, 4),
                new Die(GlassColor.YELLOW, 3)
        );

        Window window = mock(Window.class);
        when(window.getDice()).thenReturn(this.dieList);

        this.context.put(Context.WINDOW, window);
    }

    @Test
    void testNoFilterRun() {
        AtomicInteger currentIndex = new AtomicInteger();

        Map<String, String> exposedVariableMap = new HashMap<>();
        exposedVariableMap.put("die", "die_test_var");

        ForDieInstruction instruction = new ForDieInstruction(exposedVariableMap, 0, null);

        TestingInstruction testingInstruction = new TestingInstruction();
        testingInstruction.setListener((target, context) -> {
            Assertions.assertTrue(context.containsKey("die_test_var"));
            Assertions.assertEquals(Die.class, context.get("die_test_var").getClass());
            Assertions.assertSame(this.dieList.get(currentIndex.get()), context.get("die_test_var"));

            currentIndex.getAndIncrement();
        });

        instruction.setInstructions(List.of(testingInstruction));
        instruction.run(this.context);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    void testShadeFilter(Integer shadeFilter) {
        Map<String, String> exposedVariableMap = new HashMap<>();
        exposedVariableMap.put("die", "die_test_var");

        ForDieInstruction instruction = new ForDieInstruction(exposedVariableMap, shadeFilter, null);

        TestingInstruction testingInstruction = new TestingInstruction();
        testingInstruction.setListener((target, context) -> {
            Assertions.assertTrue(context.containsKey("die_test_var"));
            Assertions.assertEquals(Die.class, context.get("die_test_var").getClass());
            Assertions.assertEquals(shadeFilter, ((Die) context.get("die_test_var")).getShade());
        });

        instruction.setInstructions(List.of(testingInstruction));
        instruction.run(this.context);
    }

    @ParameterizedTest
    @EnumSource(GlassColor.class)
    void testColorFilter(GlassColor colorFilter) {
        Map<String, String> exposedVariableMap = new HashMap<>();
        exposedVariableMap.put("die", "die_test_var");

        ForDieInstruction instruction = new ForDieInstruction(exposedVariableMap, 0, colorFilter);

        TestingInstruction testingInstruction = new TestingInstruction();
        testingInstruction.setListener((target, context) -> {
            Assertions.assertTrue(context.containsKey("die_test_var"));
            Assertions.assertEquals(Die.class, context.get("die_test_var").getClass());
            Assertions.assertEquals(colorFilter, ((Die) context.get("die_test_var")).getColor());
        });

        instruction.setInstructions(List.of(testingInstruction));
        instruction.run(this.context);
    }

    @ParameterizedTest
    @MethodSource("testColorAndShadeFilterArguments")
    void testColorAndShadeFilter(Integer shadeFilter, GlassColor colorFilter) {
        Map<String, String> exposedVariableMap = new HashMap<>();
        exposedVariableMap.put("die", "die_test_var");

        ForDieInstruction instruction = new ForDieInstruction(exposedVariableMap, shadeFilter, colorFilter);

        TestingInstruction testingInstruction = new TestingInstruction();
        testingInstruction.setListener((target, context) -> {
            Assertions.assertTrue(context.containsKey("die_test_var"));
            Assertions.assertEquals(Die.class, context.get("die_test_var").getClass());
            Assertions.assertEquals(shadeFilter, ((Die) context.get("die_test_var")).getShade());
            Assertions.assertEquals(colorFilter, ((Die) context.get("die_test_var")).getColor());
        });

        instruction.setInstructions(List.of(testingInstruction));
        instruction.run(this.context);
    }
}