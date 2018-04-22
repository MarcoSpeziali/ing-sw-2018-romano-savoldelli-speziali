package it.polimi.ingsw.core.constraints;

import it.polimi.ingsw.core.Context;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstraintTest {

    @Test
    void testGetters() {
        Constraint constraint = new Constraint(
                "test_id",
                context -> 10,
                Operator.EQUALS,
                context -> 15
        );

        Assertions.assertEquals("test_id", constraint.getId());
        Assertions.assertEquals(10, constraint.getLeftOperand().get(Context.getSharedInstance()));
        Assertions.assertEquals(Operator.EQUALS, constraint.getOperator());
        Assertions.assertEquals(15, constraint.getRightOperand().get(Context.getSharedInstance()));
    }

    @Test
    void testEqual() {
        Constraint constraint = new Constraint(
                "test_id",
                context -> 10,
                Operator.EQUALS,
                context -> 15
        );

        Assertions.assertFalse(constraint.evaluate(Context.getSharedInstance()));

        constraint = new Constraint(
                "test_id",
                context -> 10,
                Operator.EQUALS,
                context -> 10
        );

        Assertions.assertTrue(constraint.evaluate(Context.getSharedInstance()));
    }

    @Test
    void testNotEqual() {
        Constraint constraint = new Constraint(
                "test_id",
                context -> 10,
                Operator.NOT_EQUALS,
                context -> 15
        );

        Assertions.assertTrue(constraint.evaluate(Context.getSharedInstance()));

        constraint = new Constraint(
                "test_id",
                context -> 10,
                Operator.NOT_EQUALS,
                context -> 10
        );

        Assertions.assertFalse(constraint.evaluate(Context.getSharedInstance()));
    }

    @Test
    void testGreater() {
        Constraint constraint = new Constraint(
                "test_id",
                context -> 10,
                Operator.GREATER,
                context -> 15
        );

        Assertions.assertFalse(constraint.evaluate(Context.getSharedInstance()));

        constraint = new Constraint(
                "test_id",
                context -> 10,
                Operator.GREATER,
                context -> 9
        );

        Assertions.assertTrue(constraint.evaluate(Context.getSharedInstance()));
    }

    @Test
    void testGreaterOrEqual() {
        Constraint constraint = new Constraint(
                "test_id",
                context -> 10,
                Operator.GREATER_EQUAL,
                context -> 15
        );

        Assertions.assertFalse(constraint.evaluate(Context.getSharedInstance()));

        constraint = new Constraint(
                "test_id",
                context -> 10,
                Operator.GREATER_EQUAL,
                context -> 10
        );

        Assertions.assertTrue(constraint.evaluate(Context.getSharedInstance()));

        constraint = new Constraint(
                "test_id",
                context -> 11,
                Operator.GREATER_EQUAL,
                context -> 10
        );

        Assertions.assertTrue(constraint.evaluate(Context.getSharedInstance()));
    }

    @Test
    void testLess() {
        Constraint constraint = new Constraint(
                "test_id",
                context -> 10,
                Operator.LESS,
                context -> 15
        );

        Assertions.assertTrue(constraint.evaluate(Context.getSharedInstance()));

        constraint = new Constraint(
                "test_id",
                context -> 10,
                Operator.LESS,
                context -> 9
        );

        Assertions.assertFalse(constraint.evaluate(Context.getSharedInstance()));
    }

    @Test
    void testLessOrEqual() {
        Constraint constraint = new Constraint(
                "test_id",
                context -> 10,
                Operator.LESS_EQUAL,
                context -> 15
        );

        Assertions.assertTrue(constraint.evaluate(Context.getSharedInstance()));

        constraint = new Constraint(
                "test_id",
                context -> 10,
                Operator.LESS_EQUAL,
                context -> 10
        );

        Assertions.assertTrue(constraint.evaluate(Context.getSharedInstance()));

        constraint = new Constraint(
                "test_id",
                context -> 11,
                Operator.LESS_EQUAL,
                context -> 10
        );

        Assertions.assertFalse(constraint.evaluate(Context.getSharedInstance()));
    }
}