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
        Die before = this.die;
        action = new IncrementAction(this.testData, this.die, 2);

        this.action.run(this.context);

        Assertions.assertEquals(before.getColor(), this.die.getColor());
        Assertions.assertEquals(5, this.die.getShade().intValue());
    }

    @Test
    void testProblematicDecrement() {
        Die before = this.die;
        action = new IncrementAction(this.testData, this.die, 4);

        this.action.run(this.context);

        Assertions.assertEquals(before.getColor(), this.die.getColor());
        Assertions.assertEquals(1, this.die.getShade().intValue());
    }

    @Test
    void testProblematicDecrement2() {
        Die before = this.die;
        action = new IncrementAction(this.testData, this.die, 5);

        this.action.run(this.context);

        Assertions.assertEquals(before.getColor(), this.die.getColor());
        Assertions.assertEquals(2, this.die.getShade().intValue());
    }

    @Test
    void testProblematicDecrement3() {
        Die before = this.die;
        action = new IncrementAction(this.testData, this.die, 6);

        this.action.run(this.context);

        Assertions.assertEquals(before.getColor(), this.die.getColor());
        Assertions.assertEquals(3, this.die.getShade().intValue());
    }
}