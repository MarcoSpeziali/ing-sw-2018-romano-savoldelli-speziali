package it.polimi.ingsw.server.actions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActionDataTest {

    private ActionData actionData;

    @BeforeEach
    void setUp() {
        this.actionData = new ActionData(
                "description_key",
                null,
                "res"
        );
    }

    @Test
    void testGetters() {
        Assertions.assertEquals("description_key", this.actionData.getDescriptionKey());

        Assertions.assertNull(this.actionData.getConstraint());

        Assertions.assertEquals("res", this.actionData.getResultIdentifier());
    }
}