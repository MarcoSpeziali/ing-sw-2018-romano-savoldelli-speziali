package it.polimi.ingsw.server.compilers.effects;

import it.polimi.ingsw.server.compilers.actions.CompiledExecutableAction;
import it.polimi.ingsw.server.constraints.EvaluableConstraint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;

class CompiledEffectTest {

    @Test
    void testGetters() {
        EvaluableConstraint evaluableConstraint = mock(EvaluableConstraint.class);
        List<CompiledExecutableAction> compiledExecutableActionList = List.of();

        CompiledEffect compiledEffect = new CompiledEffect(
                "effect_desc",
                2,
                evaluableConstraint,
                compiledExecutableActionList
        );

        Assertions.assertEquals("effect_desc", compiledEffect.getDescription());
        Assertions.assertEquals(2, compiledEffect.getInitialCost().intValue());
        Assertions.assertSame(evaluableConstraint, compiledEffect.getEvaluableConstraint());
        Assertions.assertSame(compiledExecutableActionList, compiledEffect.getCompiledExecutableActions());
    }
}