package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.UserInteractionProvider;
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
        this.die = new Die(GlassColor.MAGENTA, 3);

        UserInteractionProvider provider = mock(UserInteractionProvider.class);
        when(provider.chooseShade(this.die)).thenReturn(5);

        this.action = new SetValAction(this.testData, provider, context -> this.die);
    }

    @Test
    void run() {
        Integer preShade = this.die.getShade();

        this.action.run(this.context);

        Assertions.assertNotEquals(preShade, this.die.getShade());
        Assertions.assertEquals(5, this.die.getShade().intValue());
    }
}