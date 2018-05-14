package it.polimi.ingsw.core.instructions.predicates;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DistinctColorPredicateTest {

    @Test
    void testDistinct() {
        Die[] dice = {
                new Die(GlassColor.GREEN, 1),
                new Die(GlassColor.BLUE, 1),
                new Die(GlassColor.RED, 1),
                new Die(GlassColor.YELLOW, 1),
                new Die(GlassColor.PURPLE, 1)
        };

        DistinctColorPredicate predicate = new DistinctColorPredicate(context -> dice);
        boolean result = predicate.evaluate(Context.getSharedInstance());

        Assertions.assertTrue(result);
    }

    @Test
    void testNotDistinct() {
        Die[] dice = {
                new Die(GlassColor.GREEN, 1),
                new Die(GlassColor.GREEN, 1),
                new Die(GlassColor.RED, 1),
                new Die(GlassColor.YELLOW, 1),
                new Die(GlassColor.PURPLE, 1)
        };

        DistinctColorPredicate predicate = new DistinctColorPredicate(context -> dice);
        boolean result = predicate.evaluate(Context.getSharedInstance());

        Assertions.assertFalse(result);
    }

    @Test
    void testEmpty() {
        Die[] dice = {
        };

        DistinctColorPredicate predicate = new DistinctColorPredicate(context -> dice);
        boolean result = predicate.evaluate(Context.getSharedInstance());

        Assertions.assertTrue(result);
    }
}