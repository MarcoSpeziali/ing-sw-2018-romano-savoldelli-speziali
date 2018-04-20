package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.ChooseLocation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IncrementActionTest {

    private IncrementAction action;
    private Context context = new Context();
    private ActionData testData = new ActionData("test", null, null, null);
    private Die die;

    @BeforeEach
    void setUp() {
        this.die = new Die(GlassColor.PURPLE, 3);
    }

    @Test
    void testNonProblematicDecrement() {
        action = new IncrementAction(this.testData, this.die, 2);

        Object result = this.action.run(this.context);

        Assertions.assertEquals(Die.class, result.getClass());
        Assertions.assertSame(this.die, result);
        Assertions.assertEquals(this.die.getColor(), ((Die) result).getColor());
        Assertions.assertEquals(this.die.getShade(), ((Die) result).getShade());
        Assertions.assertEquals(5, ((Die) result).getShade().intValue());
    }

    @Test
    void testProblematicDecrement() {
        action = new IncrementAction(this.testData, this.die, 4);

        Object result = this.action.run(this.context);

        Assertions.assertEquals(Die.class, result.getClass());
        Assertions.assertSame(this.die, result);
        Assertions.assertEquals(this.die.getColor(), ((Die) result).getColor());
        Assertions.assertEquals(this.die.getShade(), ((Die) result).getShade());
        Assertions.assertEquals(1, ((Die) result).getShade().intValue());
    }

    @Test
    void testProblematicDecrement2() {
        action = new IncrementAction(this.testData, this.die, 5);

        Object result = this.action.run(this.context);

        Assertions.assertEquals(Die.class, result.getClass());
        Assertions.assertSame(this.die, result);
        Assertions.assertEquals(this.die.getColor(), ((Die) result).getColor());
        Assertions.assertEquals(this.die.getShade(), ((Die) result).getShade());
        Assertions.assertEquals(2, ((Die) result).getShade().intValue());
    }

    @Test
    void testProblematicDecrement3() {
        action = new IncrementAction(this.testData, this.die, 6);

        Object result = this.action.run(this.context);

        Assertions.assertEquals(Die.class, result.getClass());
        Assertions.assertSame(this.die, result);
        Assertions.assertEquals(this.die.getColor(), ((Die) result).getColor());
        Assertions.assertEquals(this.die.getShade(), ((Die) result).getShade());
        Assertions.assertEquals(3, ((Die) result).getShade().intValue());
    }
}