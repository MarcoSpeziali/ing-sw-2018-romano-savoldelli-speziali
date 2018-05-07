package it.polimi.ingsw.core.constraints;

import it.polimi.ingsw.core.Context;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ConstraintGroupTest {

    @Test
    void testTrueEvaluation() {
        ConstraintGroup constraintGroup = new ConstraintGroup(
                "test_id",
                List.of(
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
                )
        );

        Assertions.assertTrue(constraintGroup.evaluate(Context.getSharedInstance()));
    }

    @Test
    void testFalseEvaluation() {
        ConstraintGroup constraintGroup = new ConstraintGroup(
                "test_id",
                List.of(
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
                )
        );

        Assertions.assertFalse(constraintGroup.evaluate(Context.getSharedInstance()));
    }

    @Test
    void testGetters() {
        List<EvaluableConstraint> constraints = List.of(
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
        );

        ConstraintGroup constraintGroup = new ConstraintGroup(
                "test_id",
                constraints
        );

        Assertions.assertEquals("test_id", constraintGroup.getId());
        Assertions.assertSame(constraints, constraintGroup.getConstraints());
    }
}