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

class FlipActionTest {

    private FlipAction action;
    private Context context = new Context();
    private ActionData testData = new ActionData("test", null, null, null);
    private Die die;

    @BeforeEach
    void setUp() {
        this.die = new Die(GlassColor.PURPLE, 3);
        this.action = new FlipAction(this.testData, this.die);
    }

    @Test
    void test1() {
        this.die.setShade(1);
        Object result = this.action.run(this.context);

        Assertions.assertEquals(Die.class, result.getClass());
        Assertions.assertSame(this.die, result);
        Assertions.assertEquals(this.die.getColor(), ((Die) result).getColor());
        Assertions.assertEquals(this.die.getShade(), ((Die) result).getShade());
        Assertions.assertEquals(6, ((Die) result).getShade().intValue());
    }

    @Test
    void test2() {
        this.die.setShade(2);
        Object result = this.action.run(this.context);

        Assertions.assertEquals(Die.class, result.getClass());
        Assertions.assertSame(this.die, result);
        Assertions.assertEquals(this.die.getColor(), ((Die) result).getColor());
        Assertions.assertEquals(this.die.getShade(), ((Die) result).getShade());
        Assertions.assertEquals(5, ((Die) result).getShade().intValue());
    }

    @Test
    void test3() {
        this.die.setShade(3);
        Object result = this.action.run(this.context);

        Assertions.assertEquals(Die.class, result.getClass());
        Assertions.assertSame(this.die, result);
        Assertions.assertEquals(this.die.getColor(), ((Die) result).getColor());
        Assertions.assertEquals(this.die.getShade(), ((Die) result).getShade());
        Assertions.assertEquals(4, ((Die) result).getShade().intValue());
    }

    @Test
    void test4() {
        this.die.setShade(4);
        Object result = this.action.run(this.context);

        Assertions.assertEquals(Die.class, result.getClass());
        Assertions.assertSame(this.die, result);
        Assertions.assertEquals(this.die.getColor(), ((Die) result).getColor());
        Assertions.assertEquals(this.die.getShade(), ((Die) result).getShade());
        Assertions.assertEquals(3, ((Die) result).getShade().intValue());
    }

    @Test
    void test5() {
        this.die.setShade(5);
        Object result = this.action.run(this.context);

        Assertions.assertEquals(Die.class, result.getClass());
        Assertions.assertSame(this.die, result);
        Assertions.assertEquals(this.die.getColor(), ((Die) result).getColor());
        Assertions.assertEquals(this.die.getShade(), ((Die) result).getShade());
        Assertions.assertEquals(2, ((Die) result).getShade().intValue());
    }

    @Test
    void test6() {
        this.die.setShade(6);
        Object result = this.action.run(this.context);

        Assertions.assertEquals(Die.class, result.getClass());
        Assertions.assertSame(this.die, result);
        Assertions.assertEquals(this.die.getColor(), ((Die) result).getColor());
        Assertions.assertEquals(this.die.getShade(), ((Die) result).getShade());
        Assertions.assertEquals(1, ((Die) result).getShade().intValue());
    }
}