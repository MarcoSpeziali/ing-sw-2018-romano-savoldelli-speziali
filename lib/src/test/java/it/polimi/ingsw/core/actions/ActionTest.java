package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.constraints.Constraint;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;
import it.polimi.ingsw.core.constraints.Operator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

class ActionTest {

    private Action action;

    @BeforeEach
    void setUp() {
        this.action = mock(Action.class, CALLS_REAL_METHODS);
    }

    @Test
    void testDefaultRunThrows() {
        this.action.data = new ActionData(
                null,
                new Constraint(
                        null,
                        context -> 12,
                        Operator.EQUALS,
                        context -> 13
                ),
                null
        );

        Assertions.assertThrows(ConstraintEvaluationException.class, () -> {
            action.run(null);
        });
    }

    @Test
    void testDefaultRun() {
        this.action.data = new ActionData(
                null,
                null,
                null
        );

        Assertions.assertNull(this.action.run(null));
    }
}