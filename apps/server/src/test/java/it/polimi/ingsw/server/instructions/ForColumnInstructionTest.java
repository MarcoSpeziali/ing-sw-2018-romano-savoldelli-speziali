package it.polimi.ingsw.server.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.Window;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ForColumnInstructionTest {

    private Context context;
    private Die[][] diceMatrix;

    @BeforeEach
    @SuppressWarnings("Duplicates")
    void setUp() {
        this.context = Context.getSharedInstance();
        this.diceMatrix = new Die[][]{
                {
                        new Die(1, GlassColor.RED),
                        new Die(4, GlassColor.GREEN),
                        new Die(1, GlassColor.RED),
                        new Die(2, GlassColor.YELLOW),
                        new Die(4, GlassColor.PURPLE),
                },
                {
                        new Die(5, GlassColor.GREEN),
                        new Die(1, GlassColor.RED),
                        new Die(2, GlassColor.GREEN),
                        new Die(2, GlassColor.RED),
                        new Die(1, GlassColor.YELLOW)
                },
                {
                        new Die(5, GlassColor.RED),
                        new Die(5, GlassColor.GREEN),
                        new Die(2, GlassColor.RED),
                        new Die(3, GlassColor.YELLOW),
                        new Die(4, GlassColor.RED)
                },
                {
                        new Die(5, GlassColor.GREEN),
                        new Die(5, GlassColor.RED),
                        new Die(3, GlassColor.YELLOW),
                        new Die(4, GlassColor.RED),
                        new Die(3, GlassColor.YELLOW)
                }
        };

        Window window = mock(Window.class);
        when(window.getCells())
                .then(invocationOnMock -> Arrays.stream(this.diceMatrix)
                        .map(diceRow -> Arrays.stream(diceRow).map(die -> {
                            Cell cell = new Cell(0, null);
                            cell.putDie(die);
                            return cell;
                        }).toArray(Cell[]::new))
                        .toArray(Cell[][]::new)
                );

        this.context.put(Context.WINDOW, window);
    }

    @Test
    void testRun() {
        AtomicInteger currentIndex = new AtomicInteger();

        Map<String, String> exposedVariableMap = new HashMap<>();
        exposedVariableMap.put("column", "column_test_var");

        ForColumnInstruction instruction = new ForColumnInstruction(exposedVariableMap);

        TestingInstruction testingInstruction = new TestingInstruction();
        testingInstruction.setListener((target, context) -> {
            Assertions.assertTrue(context.containsKey("column_test_var"));
            Assertions.assertEquals(Die[].class, context.get("column_test_var").getClass());

            Die[] column = (Die[]) context.get("column_test_var");

            Assertions.assertSame(this.diceMatrix[0][currentIndex.get()], column[0]);
            Assertions.assertSame(this.diceMatrix[1][currentIndex.get()], column[1]);
            Assertions.assertSame(this.diceMatrix[2][currentIndex.get()], column[2]);
            Assertions.assertSame(this.diceMatrix[3][currentIndex.get()], column[3]);

            currentIndex.getAndIncrement();
        });

        instruction.setInstructions(List.of(testingInstruction));
        instruction.run(this.context);
    }
}