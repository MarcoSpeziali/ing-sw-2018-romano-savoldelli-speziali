package it.polimi.ingsw.models;

import it.polimi.ingsw.core.IEffect;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class ToolCardTest {

    private ToolCard toolCard;
    private IEffect effect;

    @BeforeEach
    void setUp() {

        this.effect = mock(IEffect.class);

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

    @Test
    void setCardIdTest() {
        this.toolCard.setCardId("idTest");
        Assertions.assertSame("idTest", this.toolCard.getCardId());
    }

    @Test
    void setNameKeyTest() {
        this.toolCard.setNameKey("nameTest");
        Assertions.assertSame("nameTest", this.toolCard.getNameKey());
    }

    @Test
    void setInitialCostTest() {
        this.toolCard.setInitialCost(4);
        Assertions.assertEquals(4, this.toolCard.getInitialCost());
    }

    @Test
    void setUsedOnceTest() {
        this.toolCard.setUsedOnce(Boolean.TRUE);
        Assertions.assertTrue(this.toolCard.isUsedOnce());
    }

    @Test
    void setEffectTest() {
        IEffect effect = mock(IEffect.class);
        this.toolCard.setEffect(effect);
        Assertions.assertEquals(effect, this.toolCard.getEffect());
    }
}