package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.RestrictedChoosablePutLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChoosePositionForDieActionTest {

    private ChoosePositionForDieAction action;
    private Context context = Context.getSharedInstance();
    private ActionData testData = new NullActionData();

    @BeforeEach
    void setUp() {
        RestrictedChoosablePutLocation location = mock(RestrictedChoosablePutLocation.class);
        Die die = new Die(GlassColor.YELLOW, 1);

        UserInteractionProvider interactionProvider = mock(UserInteractionProvider.class);
        when(interactionProvider.choosePosition(location, die, false, false, false)).thenReturn(6);

        action = new ChoosePositionForDieAction(this.testData, interactionProvider, context -> location, context -> die, false, false, false);
    }

    @Test
    void run() {
        Object result = this.action.run(this.context);

        Assertions.assertEquals(Integer.class, result.getClass());
        Assertions.assertEquals(6, ((Integer) result).intValue());
    }
}