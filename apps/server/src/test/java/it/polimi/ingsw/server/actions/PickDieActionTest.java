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

class PickDieActionTest {

    private PickDieAction action;
    private Context context = Context.getSharedInstance();
    private ActionData testData = new NullActionData();

    @BeforeEach
    void setUp() {
        Die die = new Die(4, GlassColor.BLUE);

        ChoosablePickLocation location = mock(ChoosablePickLocation.class);
        when(location.pickDie(die)).thenReturn(die);

        action = new PickDieAction(this.testData, context -> location, context -> die);
    }

    @Test
    void run() {
        Object result = this.action.run(this.context);

        Assertions.assertEquals(Die.class, result.getClass());
        Assertions.assertEquals(GlassColor.BLUE, ((Die) result).getColor());
        Assertions.assertEquals(4, ((Die) result).getShade().intValue());
    }
}