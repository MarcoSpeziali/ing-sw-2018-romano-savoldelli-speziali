package it.polimi.ingsw.models;

import it.polimi.ingsw.net.mocks.IEffect;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ToolCardTest {

    private ToolCard toolCard;
    private IEffect effect;

    @BeforeEach
    void setUp() {

        this.effect = mock(IEffect.class);

        this.toolCard = new ToolCard("testId", "test_card", "descriptionTest", effect);
    }

    @Test
    void getCardIdTest() {
        Assertions.assertEquals("testId", toolCard.getCardId());
    }

    @Test
    void getEffect() {
        Assertions.assertEquals(effect, toolCard.getEffect());
    }

    @Test
    void activate() {
        doNothing().when(this.effect).run(this.toolCard.getCardId());

        toolCard.activate();

        verify(this.effect, times(1)).run(this.toolCard.getCardId());
    }

    @Test
    void setCardIdTest() {
        this.toolCard.setCardId("idTest");
        Assertions.assertSame("idTest", this.toolCard.getCardId());
    }

    @Test
    void setEffectTest() {
        IEffect effect = mock(IEffect.class);
        this.toolCard.setEffect(effect);
        Assertions.assertEquals(effect, this.toolCard.getEffect());
    }
}