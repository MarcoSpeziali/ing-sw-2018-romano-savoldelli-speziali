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
                new Die(1, GlassColor.GREEN),
                new Die(2, GlassColor.BLUE),
                new Die(3, GlassColor.RED),
                new Die(4, GlassColor.YELLOW),
                new Die(5, GlassColor.PURPLE)
        };

        DistinctShadePredicate predicate = new DistinctShadePredicate(context -> dice);
        boolean result = predicate.evaluate(Context.getSharedInstance());

        Assertions.assertTrue(result);
    }

    @Test
    void testNotDistinct() {
        Die[] dice = {
                new Die(1, GlassColor.GREEN),
                new Die(1, GlassColor.GREEN),
                new Die(2, GlassColor.RED),
                new Die(3, GlassColor.YELLOW),
                new Die(5, GlassColor.PURPLE)
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