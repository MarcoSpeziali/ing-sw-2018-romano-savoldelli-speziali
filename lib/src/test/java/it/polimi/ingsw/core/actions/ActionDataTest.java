package it.polimi.ingsw.core.actions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActionDataTest {

    private ActionData actionData;

    @BeforeEach
    void setUp() {
        this.actionData = new ActionData(
                "id",
                "next_action_id",
                "description_key",
                null,
                "res"
        );
    }

    @Test
    void testGetters() {
        Assertions.assertEquals("id", this.actionData.getId());
        Assertions.assertEquals("next_action_id", this.actionData.getNextActionId());
        Assertions.assertEquals("description_key", this.actionData.getDescriptionKey());

        Assertions.assertNull(this.actionData.getConstraint());

        Assertions.assertEquals("res", this.actionData.getResultIdentifier());
    }
}