package it.polimi.ingsw.server.instructions.predicates;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DistinctShadePredicateTest {

    @Test
    void testDistinct() {
        Die[] dice = {
                new Die(GlassColor.GREEN, 1),
                new Die(GlassColor.BLUE, 2),
                new Die(GlassColor.RED, 3),
                new Die(GlassColor.YELLOW, 4),
                new Die(GlassColor.PURPLE, 5)
        };

        DistinctShadePredicate predicate = new DistinctShadePredicate(context -> dice);
        boolean result = predicate.evaluate(Context.getSharedInstance());

        Assertions.assertTrue(result);
    }

    @Test
    void testNotDistinct() {
        Die[] dice = {
                new Die(GlassColor.GREEN, 1),
                new Die(GlassColor.GREEN, 1),
                new Die(GlassColor.RED, 2),
                new Die(GlassColor.YELLOW, 3),
                new Die(GlassColor.PURPLE, 5)
        };

        DistinctShadePredicate predicate = new DistinctShadePredicate(context -> dice);
        boolean result = predicate.evaluate(Context.getSharedInstance());

        Assertions.assertFalse(result);
    }

    @Test
    void testEmpty() {
        Die[] dice = {
        };

        DistinctShadePredicate predicate = new DistinctShadePredicate(context -> dice);
        boolean result = predicate.evaluate(Context.getSharedInstance());

        Assertions.assertTrue(result);
    }
}