package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.ChooseLocation;
import it.polimi.ingsw.core.locations.RestrictedChoosablePutLocation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.booleanThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChoosePositionForDieActionTest {

    private ChoosePositionForDieAction action;
    private Context context = new Context();
    private ActionData testData = new ActionData("test", null, null, null);

    @BeforeEach
    void setUp() {
        RestrictedChoosablePutLocation location = mock(RestrictedChoosablePutLocation.class);
        Die die = new Die(GlassColor.YELLOW, 1);

        UserInteractionProvider interactionProvider = mock(UserInteractionProvider.class);
        when(interactionProvider.choosePosition(location, die, false, false, false)).thenReturn(6);

        action = new ChoosePositionForDieAction(this.testData, interactionProvider, location, die, false, false, false);
    }

    @Test
    void run() {
        Object result = this.action.run(this.context);

        Assertions.assertEquals(Integer.class, result.getClass());
        Assertions.assertEquals(6, ((Integer) result).intValue());
    }
}