package it.polimi.ingsw.server;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.server.instructions.ForDieInstruction;
import it.polimi.ingsw.server.instructions.Instruction;
import it.polimi.ingsw.server.instructions.TakeInstruction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ObjectiveTest {

    private Objective objective;
    private List<Instruction> instructions;

    @BeforeEach
    void setUp() {
        ForDieInstruction forDieInstruction = new ForDieInstruction(
                Map.of("die", "DIE"),
                0,
                GlassColor.GREEN
        );
        forDieInstruction.setInstructions(
                List.of(new TakeInstruction())
        );

        this.instructions = List.of(forDieInstruction);

        this.objective = new Objective(
                5,
                "desc",
                this.instructions
        );

        Window window = mock(Window.class);
        Context.getSharedInstance().put(Context.WINDOW, window);

        when(window.getDice()).thenReturn(List.of(
                new Die(1, GlassColor.RED),
                new Die(2, GlassColor.RED),
                new Die(3, GlassColor.GREEN),
                new Die(4, GlassColor.RED),
                new Die(5, GlassColor.GREEN),
                new Die(6, GlassColor.RED),
                new Die(1, GlassColor.GREEN)
        ));
    }

    @Test
    void testGetters() {
        Assertions.assertEquals("desc", this.objective.getDescription().toString());
        Assertions.assertEquals(5, this.objective.getPointsPerCompletion());
        Assertions.assertSame(this.instructions, this.objective.getInstructions());
    }

    @Test
    void testPointsCalculation() {
        Assertions.assertEquals(15, this.objective.calculatePoints(Context.getSharedInstance()));
    }
}