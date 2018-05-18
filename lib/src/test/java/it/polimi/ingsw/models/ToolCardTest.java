package it.polimi.ingsw.models;

import it.polimi.ingsw.core.Effect;
import it.polimi.ingsw.utils.text.LocalizedString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ToolCardTest {

    private ToolCard toolCard;
    private Effect effect;

    @BeforeEach
    void setUp() {

        this.effect = mock(Effect.class);

        this.toolCard = new ToolCard("testId", "test_card", 3, effect ,
                "titleTest", "descriptionTest");
    }

    @Test
    void getCardIdTest() {
        Assertions.assertEquals("testId", toolCard.getCardId());
    }

    @Test
    void getNameKeyTest() {
        Assertions.assertEquals("test_card", toolCard.getNameKey());
    }

    @Test
    void getInitialCost() {
        Assertions.assertEquals(3, toolCard.getInitialCost());
    }

    @Test
    void isUsedOnceTest() {
        Assertions.assertFalse(toolCard.isUsedOnce());
    }

    @Test
    void getEffect() {
        Assertions.assertEquals(effect, toolCard.getEffect());
    }

    @Test
    void activate() {
        toolCard.activate();
        Assertions.assertTrue(toolCard.isUsedOnce());
    }
}