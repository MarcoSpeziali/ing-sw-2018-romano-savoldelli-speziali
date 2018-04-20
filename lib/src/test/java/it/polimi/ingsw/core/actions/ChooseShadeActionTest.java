package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.ChooseLocation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChooseShadeActionTest {

    private ChooseShadeAction action;
    private Context context = new Context();
    private ActionData testData = new ActionData("test", null, null, null);

    @BeforeEach
    void setUp() {
        ChooseLocation location = mock(ChooseLocation.class);

        UserInteractionProvider interactionProvider = mock(UserInteractionProvider.class);
        when(interactionProvider.chooseDie(location, null, 0))
                .thenReturn(new Die(GlassColor.GREEN, 4));

        action = new ChooseShadeAction(this.testData, interactionProvider, location, null);
    }

    @Test
    void run() {
        Object result = this.action.run(this.context);

        Assertions.assertEquals(Integer.class, result.getClass());
        Assertions.assertEquals(4, ((Integer) result).intValue());
    }
}