package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SwapActionTest {

    private SwapAction action;
    private Context context = Context.getSharedInstance();
    private ActionData testData = new NullActionData();
    private Die die1;
    private Die die2;

    @BeforeEach
    void setUp() {
        this.die1 = new Die(GlassColor.PURPLE, 3);
        this.die2 = new Die(GlassColor.YELLOW, 4);

        this.action = new SwapAction(this.testData, context -> this.die1, context -> this.die2);
    }

    @Test
    void run() {
        GlassColor preColor1 = this.die1.getColor();
        GlassColor preColor2 = this.die2.getColor();
        Integer preShade1 = this.die1.getShade();
        Integer preShade2 = this.die2.getShade();

        this.action.run(this.context);

        Assertions.assertEquals(preColor1, this.die2.getColor());
        Assertions.assertEquals(preColor2, this.die1.getColor());

        Assertions.assertEquals(preShade1, this.die2.getShade());
        Assertions.assertEquals(preShade2, this.die1.getShade());
    }
}