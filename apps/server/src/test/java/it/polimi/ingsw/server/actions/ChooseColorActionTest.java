package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.ChooseLocation;
import it.polimi.ingsw.models.Die;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChooseColorActionTest {

    private ChooseColorAction action;
    private Context context = Context.getSharedInstance();
    private ActionData testData = new NullActionData();

    @BeforeEach
    void setUp() {
        ChooseLocation location = mock(ChooseLocation.class);

        UserInteractionProvider interactionProvider = mock(UserInteractionProvider.class);
        when(interactionProvider.chooseDie(location, null, 0))
                .thenReturn(new Die(GlassColor.GREEN, 4));

        action = new ChooseColorAction(this.testData, context -> location, context -> 0);
        action.setUserInteractionProvider(interactionProvider);
    }

    @Test
    void run() {
        Object result = this.action.run(this.context);

        Assertions.assertEquals(GlassColor.class, result.getClass());
        Assertions.assertEquals(GlassColor.GREEN, result);
    }
}