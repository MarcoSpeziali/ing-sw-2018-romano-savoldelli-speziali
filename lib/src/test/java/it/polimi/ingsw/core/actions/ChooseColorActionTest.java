package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.ChooseLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChooseColorActionTest {

    private ChooseColorAction action;
    private Context context = new Context();
    private ActionData testData = new ActionData("test", null, null, null);

    @BeforeEach
    void setUp() {
        ChooseLocation location = mock(ChooseLocation.class);

        UserInteractionProvider interactionProvider = mock(UserInteractionProvider.class);
        when(interactionProvider.chooseDie(location, null, 0))
                .thenReturn(new Die(GlassColor.GREEN, 4));

        action = new ChooseColorAction(this.testData, interactionProvider, location, 0);
    }

    @Test
    void run() {
        Object result = this.action.run(this.context);

        Assertions.assertEquals(GlassColor.class, result.getClass());
        Assertions.assertEquals(GlassColor.GREEN, result);
    }
}