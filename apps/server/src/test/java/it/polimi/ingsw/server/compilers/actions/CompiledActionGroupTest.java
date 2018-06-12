package it.polimi.ingsw.server.compilers.actions;

import it.polimi.ingsw.server.actions.ActionData;
import it.polimi.ingsw.server.actions.ActionGroup;
import it.polimi.ingsw.utils.IterableRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CompiledActionGroupTest {

    private CompiledActionGroup compiledActionGroup;
    private ActionData actionData;
    private List<CompiledExecutableAction> compiledExecutableActions;

    @BeforeEach
    void setUp() {
        this.actionData = mock(ActionData.class);
        this.compiledExecutableActions = List.of();

        this.compiledActionGroup = new CompiledActionGroup(
                this.actionData,
                this.compiledExecutableActions,
                IterableRange.unitaryInteger(),
                IterableRange.unitaryInteger()
        );
    }

    @Test
    void getActionData() {
        Assertions.assertSame(this.actionData, this.compiledActionGroup.getActionData());
        Assertions.assertSame(this.compiledExecutableActions, this.compiledActionGroup.getActions());
        Assertions.assertEquals(ActionGroup.class, this.compiledActionGroup.getClassToInstantiate());
        Assertions.assertEquals(IterableRange.unitaryInteger(), this.compiledActionGroup.getRepetitions());
        Assertions.assertEquals(IterableRange.unitaryInteger(), this.compiledActionGroup.getChooseBetween());
    }
}