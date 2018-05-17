package it.polimi.ingsw.models;

import it.polimi.ingsw.core.Effect;
import it.polimi.ingsw.utils.text.LocalizedString;
import javafx.scene.image.Image;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/*class CardTest {

    private ToolCard toolCard;
    private Effect effect;
    private Image image;
    private LocalizedString title_test;
    private LocalizedString description_test;

    @BeforeEach
    void setUp() {

        this.image = mock(Image.class);
        this.effect = mock(Effect.class);
        this.description_test = mock(LocalizedString.class);
        this.title_test = mock(LocalizedString.class);

        //this.toolCard = new ToolCard("testId", "test_card", 3, effect , image,
               //image,  title_test, description_test);
    }

    @Test
    void getBackImageTest() {
       Assertions.assertEquals(image, toolCard.getBackImage());
    }

    @Test
    void getFrontImageTest() {
        Assertions.assertEquals(image, toolCard.getFrontImage());
    }

    @Test
    void getTitleTest() {
        Assertions.assertEquals(title_test, toolCard.getTitle());
    }

    @Test
    void getDescriptionTest() {
        Assertions.assertEquals(description_test, toolCard.getDescription());
    }

    @Test
    void cardConstructorTest()
    {
        String backImagePath = "testBackPath";
        String frontImagePath = "testFrontPath";
        this.title_test = mock(LocalizedString.class);
        this.description_test = mock(LocalizedString.class);

        //CardImpl cardimp = new CardImpl(backImagePath, frontImagePath, title_test, description_test);
        Assertions.assertEquals(title_test, cardimp.title);
        Assertions.assertEquals(description_test, cardimp.description);
    }

    private class CardImpl extends Card {

       // public CardImpl(Image backImage, Image frontImage, LocalizedString title, LocalizedString description) {
       //     super(backImage, frontImage, title, description);
        }

      //  public CardImpl(String backImagePath, String frontImagePath, LocalizedString title, LocalizedString description) {
      //      super(backImagePath, frontImagePath, title, description);

        }
    }

}*/