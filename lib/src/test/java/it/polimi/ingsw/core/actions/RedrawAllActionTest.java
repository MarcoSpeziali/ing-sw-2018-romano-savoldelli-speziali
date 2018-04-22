package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.ChooseLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RedrawAllActionTest {

    private RedrawAllAction action;
    private Context context = Context.getSharedInstance();
    private ActionData testData = new ActionData("test", null, null, null, null);
    private Die[] dice;

    @BeforeEach
    void setUp() {
        Random random = new Random(System.currentTimeMillis());

        dice = random
                .ints(10, 1, 7)
                .mapToObj(num -> new Die(GlassColor.values()[random.nextInt(5)], num))
                .toArray(Die[]::new);

        ChooseLocation location = mock(ChooseLocation.class);
        when(location.getDice()).thenReturn(Arrays.stream(dice).collect(Collectors.toList()));

        this.action = new RedrawAllAction(this.testData, location);
    }

    @Test
    void run() {
        Die[] before = this.dice.clone();

        this.action.run(this.context);

        Assertions.assertEquals(this.dice.length, this.dice.length);

        for (int i = 0; i < this.dice.length; i++) {
            Assertions.assertEquals(this.dice[i].getColor(), before[i].getColor());
        }
    }
}