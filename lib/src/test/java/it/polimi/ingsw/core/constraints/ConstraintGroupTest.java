package it.polimi.ingsw.core.constraints;

import it.polimi.ingsw.core.Context;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

class ConstraintGroupTest {

    @Test
    void testTrueEvaluation() {
        ConstraintGroup constraintGroup = new ConstraintGroup(
                "test_id",
                Stream.of(
                        new Constraint(
                                "test_id",
                                context -> 10,
                                Operator.LESS,
                                context -> 15
                        ),
                        new Constraint(
                                "test_id",
                                context -> 10,
                                Operator.NOT_EQUALS,
                                context -> 15
                        )
                ).collect(Collectors.toList())
        );

        Assertions.assertTrue(constraintGroup.evaluate(Context.getSharedInstance()));
    }

    @Test
    void testFalseEvaluation() {
        ConstraintGroup constraintGroup = new ConstraintGroup(
                "test_id",
                Stream.of(
                        new Constraint(
                                "test_id",
                                context -> 19,
                                Operator.LESS,
                                context -> 15
                        ),
                        new Constraint(
                                "test_id",
                                context -> 10,
                                Operator.NOT_EQUALS,
                                context -> 15
                        )
                ).collect(Collectors.toList())
        );

        Assertions.assertFalse(constraintGroup.evaluate(Context.getSharedInstance()));
    }
}