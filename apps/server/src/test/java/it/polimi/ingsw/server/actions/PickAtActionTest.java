package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.ChoosablePickLocation;
import it.polimi.ingsw.models.Die;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PickAtActionTest {

    private PickAtAction action;
    private Context context = Context.getSharedInstance();
    private ActionData testData = new NullActionData();

    @BeforeEach
    void setUp() {
        ChoosablePickLocation location = mock(ChoosablePickLocation.class);
        when(location.pickDie(0)).thenReturn(new Die(4, GlassColor.BLUE));

        action = new PickAtAction(this.testData, context -> location, context -> 0);
    }

    @Test
    void run() {
        Object result = this.action.run(this.context);

        Assertions.assertEquals(Die.class, result.getClass());
        Assertions.assertEquals(GlassColor.BLUE, ((Die) result).getColor());
        Assertions.assertEquals(4, ((Die) result).getShade().intValue());
    }
}