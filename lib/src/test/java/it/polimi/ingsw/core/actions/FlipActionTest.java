package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlipActionTest {

    private FlipAction action;
    private Context context = Context.getSharedInstance();
    private ActionData testData = new ActionData("test", null, null, null, null);
    private Die die;

    @BeforeEach
    void setUp() {
        this.die = new Die(GlassColor.PURPLE, 3);
        this.action = new FlipAction(this.testData, context -> this.die);
    }

    @Test
    void test1() {
        this.die.setShade(1);
        GlassColor preColor = this.die.getColor();
        Integer preShade = this.die.getShade();

        this.action.run(this.context);

        Assertions.assertEquals(preColor, this.die.getColor());
        Assertions.assertNotEquals(preShade, this.die.getShade());
        Assertions.assertEquals(6, this.die.getShade().intValue());
    }

    @Test
    void test2() {
        this.die.setShade(2);
        GlassColor preColor = this.die.getColor();
        Integer preShade = this.die.getShade();

        this.action.run(this.context);

        Assertions.assertEquals(preColor, this.die.getColor());
        Assertions.assertNotEquals(preShade, this.die.getShade());
        Assertions.assertEquals(5, this.die.getShade().intValue());
    }

    @Test
    void test3() {
        this.die.setShade(3);
        GlassColor preColor = this.die.getColor();
        Integer preShade = this.die.getShade();

        this.action.run(this.context);

        Assertions.assertEquals(preColor, this.die.getColor());
        Assertions.assertNotEquals(preShade, this.die.getShade());
        Assertions.assertEquals(4, this.die.getShade().intValue());
    }

    @Test
    void test4() {
        this.die.setShade(4);
        GlassColor preColor = this.die.getColor();
        Integer preShade = this.die.getShade();

        this.action.run(this.context);

        Assertions.assertEquals(preColor, this.die.getColor());
        Assertions.assertNotEquals(preShade, this.die.getShade());
        Assertions.assertEquals(3, this.die.getShade().intValue());
    }

    @Test
    void test5() {
        this.die.setShade(5);
        GlassColor preColor = this.die.getColor();
        Integer preShade = this.die.getShade();

        this.action.run(this.context);

        Assertions.assertEquals(preColor, this.die.getColor());
        Assertions.assertNotEquals(preShade, this.die.getShade());
        Assertions.assertEquals(2, this.die.getShade().intValue());
    }

    @Test
    void test6() {
        this.die.setShade(6);
        GlassColor preColor = this.die.getColor();
        Integer preShade = this.die.getShade();

        this.action.run(this.context);

        Assertions.assertEquals(preColor, this.die.getColor());
        Assertions.assertNotEquals(preShade, this.die.getShade());
        Assertions.assertEquals(1, this.die.getShade().intValue());
    }
}