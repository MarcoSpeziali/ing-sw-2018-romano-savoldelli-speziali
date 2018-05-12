package it.polimi.ingsw.core;

import it.polimi.ingsw.core.actions.*;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.utils.IterableRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class EffectTest {

    @Test
    void testGetters() {
        List<ExecutableAction> actions = List.of(
                new DecrementAction(
                        null,
                        null,
                        null
                ),
                new IncrementAction(
                        null,
                        null,
                        null
                )
        );

        Effect effect = new Effect(
                "localized_string.test.test_getters",
                null,
                actions
        );

        Assertions.assertEquals("Example", effect.getDescription());
        Assertions.assertNull(effect.getEffectConstraint());
        Assertions.assertArrayEquals(actions.toArray(), effect.getActions().toArray());
    }

    @Test
    void run() {
        Die die = new Die(GlassColor.PURPLE, 3);

        List<ExecutableAction> actions = List.of(
                new DecrementAction(
                        new ActionData(
                                null,
                                null,
                                null
                        ),
                        context -> die,
                        context -> 2 // 1
                ),
                new IncrementAction(
                        new ActionData(
                                null,
                                null,
                                null
                        ),
                        context -> die,
                        context -> 3 // 4
                ),
                new FlipAction(
                        new ActionData(
                                null,
                                null,
                                null
                        ),
                        context -> die // 3
                ),
                new ActionGroup(
                        new ActionData(
                                null,
                                null,
                                null
                        ),
                        IterableRange.singleValued(3, val -> ++val),
                        null,
                        List.of(
                                new DecrementAction(
                                        new ActionData(
                                                null,
                                                null,
                                                null
                                        ),
                                        context -> die,
                                        context -> 2 // 1, 5, 5
                                ),
                                new IncrementAction(
                                        new ActionData(
                                                null,
                                                null,
                                                null
                                        ),
                                        context -> die,
                                        context -> 5 // 6, 4, 4
                                ),
                                new FlipAction(
                                        new ActionData(
                                                null,
                                                null,
                                                null
                                        ),
                                        context -> die // 1, 3, 3
                                )
                        ),
                        null
                ),
                new IncrementAction(
                        new ActionData(
                                null,
                                null,
                                null
                        ),
                        context -> die,
                        context -> 3 // 6
                )
        );

        new Effect(
                null,
                null,
                actions
        ).run("test");

        Assertions.assertEquals(4, die.getShade().intValue());
    }
}