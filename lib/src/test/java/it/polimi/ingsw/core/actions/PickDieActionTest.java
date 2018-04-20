package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.ChoosablePickLocation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PickDieActionTest {

    private PickDieAction action;
    private Context context = new Context();
    private ActionData testData = new ActionData("test", null, null, null);

    @BeforeEach
    void setUp() {
        Die die = new Die(GlassColor.BLUE, 4);

        ChoosablePickLocation location = mock(ChoosablePickLocation.class);
        when(location.pickDie(die)).thenReturn(die);

        action = new PickDieAction(this.testData, location, die);
    }

    @Test
    void run() {
        Object result = this.action.run(this.context);

        Assertions.assertEquals(Die.class, result.getClass());
        Assertions.assertEquals(GlassColor.BLUE, ((Die) result).getColor());
        Assertions.assertEquals(4, ((Die) result).getShade().intValue());
    }
}