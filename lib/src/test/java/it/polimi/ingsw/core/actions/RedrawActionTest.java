package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RedrawActionTest {

    private RedrawAction action;
    private Context context = new Context();
    private ActionData testData = new ActionData("test", null, null, null);
    private Die die;

    @BeforeEach
    void setUp() {
        this.die = new Die(GlassColor.PURPLE, 3);
        this.action = new RedrawAction(this.testData, this.die);
    }

    @Test
    void run() {
        Object result = this.action.run(this.context);

        Assertions.assertEquals(Die.class, result.getClass());
        Assertions.assertSame(this.die, result);
        Assertions.assertEquals(this.die.getColor(), ((Die) result).getColor());
    }
}