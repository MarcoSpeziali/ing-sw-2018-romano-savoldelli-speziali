package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.ChooseLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChoosePositionActionTest {

    private ChoosePositionAction action;
    private Context context = Context.getSharedInstance();
    private ActionData testData = new NullActionData();

    @BeforeEach
    void setUp() {
        ChooseLocation location = mock(ChooseLocation.class);

        UserInteractionProvider interactionProvider = mock(UserInteractionProvider.class);
        when(interactionProvider.choosePosition(location)).thenReturn(6);

        action = new ChoosePositionAction(this.testData, context -> location, context -> null, context -> 0);
        action.setUserInteractionProvider(interactionProvider);
    }

    @Test
    void run() {
        Object result = this.action.run(this.context);

        Assertions.assertEquals(Integer.class, result.getClass());
        Assertions.assertEquals(6, ((Integer) result).intValue());
    }
}