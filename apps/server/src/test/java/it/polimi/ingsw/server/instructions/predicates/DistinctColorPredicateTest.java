package it.polimi.ingsw.server.instructions.predicates;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DistinctColorPredicateTest {

    @Test
    void testDistinct() {
        Die[] dice = {
                new Die(1, GlassColor.GREEN),
                new Die(1, GlassColor.BLUE),
                new Die(1, GlassColor.RED),
                new Die(1, GlassColor.YELLOW),
                new Die(1, GlassColor.PURPLE)
        };

        DistinctColorPredicate predicate = new DistinctColorPredicate(context -> dice);
        boolean result = predicate.evaluate(Context.getSharedInstance());

        Assertions.assertTrue(result);
    }

    @Test
    void testNotDistinct() {
        Die[] dice = {
                new Die(1, GlassColor.GREEN),
                new Die(1, GlassColor.GREEN),
                new Die(1, GlassColor.RED),
                new Die(1, GlassColor.YELLOW),
                new Die(1, GlassColor.PURPLE)
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