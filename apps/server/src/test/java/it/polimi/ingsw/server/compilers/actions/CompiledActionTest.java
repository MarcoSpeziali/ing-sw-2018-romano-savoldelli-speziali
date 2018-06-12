package it.polimi.ingsw.server.compilers.actions;

import it.polimi.ingsw.server.actions.Action;
import it.polimi.ingsw.server.actions.ActionData;
import it.polimi.ingsw.server.compilers.commons.CompiledParameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CompiledActionTest {

    private CompiledAction compiledAction;
    private ActionData actionData;
    private List<CompiledParameter> compiledParameters;

    @BeforeEach
    void setUp() {
        this.actionData = mock(ActionData.class);
        this.compiledParameters = List.of();

        this.compiledAction = new CompiledAction(
                "actionId",
                Action.class,
                this.actionData,
                this.compiledParameters
        );
    }

    @Test
    void testGetters() {
        Assertions.assertEquals("actionId", this.compiledAction.getActionId());
        Assertions.assertEquals(Action.class, this.compiledAction.getClassToInstantiate());
        Assertions.assertSame(this.actionData, this.compiledAction.getActionData());
        Assertions.assertSame(this.compiledParameters, this.compiledAction.getParameters());
    }
}