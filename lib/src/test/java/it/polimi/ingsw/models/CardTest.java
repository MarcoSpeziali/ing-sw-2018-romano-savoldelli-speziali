package it.polimi.ingsw.models;

import it.polimi.ingsw.net.mocks.IEffect;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class CardTest {

    private ToolCard toolCard;
    private IEffect effect;
    private String description_test;

    @BeforeEach
    void setUp() {

        this.effect = mock(IEffect.class);
        this.description_test = "descriptionTest";

        this.toolCard = new ToolCard("testId", "test_card", description_test, effect);
    }

    @Test
    void getDescriptionTest() {
        Assertions.assertEquals(description_test, toolCard.getDescription());
    }

    @Test
    void cardConstructorTest() {
        this.description_test = "descriptionTest";

        CardImpl cardImp = new CardImpl("test_card", description_test);
        Assertions.assertEquals("test_card", cardImp.title);
        Assertions.assertEquals(description_test, cardImp.description);

    }

    @Test
    void getTitle() {
        Assertions.assertEquals("test_card", this.toolCard.getTitle());
    }

    @Test
    void getDescription() {
        Assertions.assertEquals("descriptionTest", this.toolCard.getDescription());

    }

    @Test
    void setTitle() {
        this.toolCard.setTitle("setTestTitle");
        Assertions.assertSame("setTestTitle", this.toolCard.getTitle());
    }

    @Test
    void setDescription() {
        this.toolCard.setDescription("setTestDescription");
        Assertions.assertSame("setTestDescription", this.toolCard.getDescription());
    }

    private class CardImpl extends Card {
        public CardImpl(String title, String description) {
            super(title, description);
        }
    }

}