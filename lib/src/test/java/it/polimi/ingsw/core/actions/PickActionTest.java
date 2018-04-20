package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.ChooseLocation;
import it.polimi.ingsw.core.locations.PickLocation;
import it.polimi.ingsw.core.locations.RandomPickLocation;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PickActionTest {

    private PickAction action;
    private Context context = new Context();
    private ActionData testData = new ActionData("test", null, null, null);

    @BeforeEach
    void setUp() {
        RandomPickLocation location = mock(RandomPickLocation.class);
        when(location.pickDie()).thenReturn(new Die(GlassColor.BLUE, 4));

        action = new PickAction(this.testData, location);
    }

    @Test
    void run() {
        Object result = this.action.run(this.context);

        Assertions.assertEquals(Die.class, result.getClass());
        Assertions.assertEquals(GlassColor.BLUE, ((Die) result).getColor());
        Assertions.assertEquals(4, ((Die) result).getShade().intValue());
    }
}