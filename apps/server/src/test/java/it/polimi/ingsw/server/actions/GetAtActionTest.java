package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.ChooseLocation;
import it.polimi.ingsw.models.Die;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetAtActionTest {

    @Test
    void testGetAt() {
        Die die = new Die(5, GlassColor.RED);

        ChooseLocation chooseLocation = mock(ChooseLocation.class);
        when(chooseLocation.getDie(5)).thenReturn(die);

        GetAtAction getAtAction = new GetAtAction(new ActionData(null, null, null), context -> chooseLocation, context -> 5);
        Die getDie = (Die) getAtAction.run(Context.getSharedInstance());

        Assertions.assertSame(die, getDie);
    }
}