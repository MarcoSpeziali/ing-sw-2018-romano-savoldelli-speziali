package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IncrementActionTest {

    private IncrementAction action;
    private Context context = Context.getSharedInstance();
    private ActionData testData = new NullActionData();
    private Die die;

    @BeforeEach
    void setUp() {
        this.die = new Die(GlassColor.PURPLE, 3);
    }

    @Test
    void testNonProblematicDecrement() {
        Die before = this.die;
        action = new IncrementAction(this.testData, context -> this.die, context -> 2);

        this.action.run(this.context);

        Assertions.assertEquals(before.getColor(), this.die.getColor());
        Assertions.assertEquals(5, this.die.getShade().intValue());
    }

    @Test
    void testProblematicDecrement() {
        Die before = this.die;
        action = new IncrementAction(this.testData, context -> this.die, context -> 4);

        this.action.run(this.context);

        Assertions.assertEquals(before.getColor(), this.die.getColor());
        Assertions.assertEquals(1, this.die.getShade().intValue());
    }

    @Test
    void testProblematicDecrement2() {
        Die before = this.die;
        action = new IncrementAction(this.testData, context -> this.die, context -> 5);

        this.action.run(this.context);

        Assertions.assertEquals(before.getColor(), this.die.getColor());
        Assertions.assertEquals(2, this.die.getShade().intValue());
    }

    @Test
    void testProblematicDecrement3() {
        Die before = this.die;
        action = new IncrementAction(this.testData, context -> this.die, context -> 6);

        this.action.run(this.context);

        Assertions.assertEquals(before.getColor(), this.die.getColor());
        Assertions.assertEquals(3, this.die.getShade().intValue());
    }
}