package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.models.Die;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SetValActionTest {

    private SetValAction action;
    private Context context = Context.getSharedInstance();
    private ActionData testData = new NullActionData();
    private Die die;

    @BeforeEach
    void setUp() {
        this.die = new Die(3, GlassColor.PURPLE);

        UserInteractionProvider provider = mock(UserInteractionProvider.class);
        when(provider.chooseShade(this.die)).thenReturn(5);

        this.action = new SetValAction(this.testData, context -> this.die);
        this.action.setUserInteractionProvider(provider);
    }

    @Test
    void run() {
        Integer preShade = this.die.getShade();

        this.action.run(this.context);

        Assertions.assertNotEquals(preShade, this.die.getShade());
        Assertions.assertEquals(5, this.die.getShade().intValue());
    }
}