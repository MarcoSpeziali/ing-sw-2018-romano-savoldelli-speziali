package it.polimi.ingsw.models;

import it.polimi.ingsw.core.IEffect;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class CardTest {

    private ToolCard toolCard;
    private IEffect effect;
    private String title_test;
    private String description_test;

    @BeforeEach
    void setUp() {

        this.effect = mock(IEffect.class);
        this.description_test = "descriptionTest";
        this.title_test = "titleTest";

        this.toolCard = new ToolCard("testId", "test_card", 3, effect, title_test, description_test);
    }

    @Test
    void getTitleTest() {
        Assertions.assertEquals(title_test, toolCard.getTitle().toString());
    }

    @Test
    void getDescriptionTest() {
        Assertions.assertEquals(description_test, toolCard.getDescription().toString());
    }

    @Test
    void cardConstructorTest()
    {
        this.title_test = "testTitle";
        this.description_test = "descriptionTest";

        CardImpl cardImp = new CardImpl(title_test, description_test);
        Assertions.assertEquals(title_test, cardImp.title.toString());
        Assertions.assertEquals(description_test, cardImp.description.toString());

    }

    @Test
    void getTitle() {
        Assertions.assertEquals("titleTest", this.toolCard.getTitle().toString());
    }

    @Test
    void getDescription() {
        Assertions.assertEquals("descriptionTest", this.toolCard.getDescription().toString());

    }

    @Test
    void setTitle() {
        this.toolCard.setTitle("setTestTitle");
        Assertions.assertSame("setTestTitle", this.toolCard.getTitle().getLocalizationKey());
    }

    @Test
    void setDescription() {
        this.toolCard.setDescription("setTestDescription");
        Assertions.assertSame("setTestDescription", this.toolCard.getDescription().getLocalizationKey());
    }

    private class CardImpl extends Card {

        public CardImpl(String title, String description) {
            super(title, description);
        }
    }

}