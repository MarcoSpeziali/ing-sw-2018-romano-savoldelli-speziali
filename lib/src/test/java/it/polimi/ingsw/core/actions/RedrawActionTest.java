package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RedrawActionTest {

    private RedrawAction action;
    private Context context = Context.getSharedInstance();
    private ActionData testData = new NullActionData();
    private Die die;

    @BeforeEach
    void setUp() {
        this.die = new Die(GlassColor.MAGENTA, 3);
        this.action = new RedrawAction(this.testData, context -> this.die);
    }

    @Test
    void run() {
        GlassColor preColor = this.die.getColor();
        this.action.run(this.context);

        Assertions.assertEquals(this.die.getColor(), preColor);
    }
}