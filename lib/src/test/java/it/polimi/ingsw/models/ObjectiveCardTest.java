package it.polimi.ingsw.models;

import it.polimi.ingsw.core.CardVisibility;
import it.polimi.ingsw.core.IObjective;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class ObjectiveCardTest {

    private ObjectiveCard objectiveCard;
    private IObjective obj;

    @BeforeEach
    void setUp() {
        this.obj = mock(IObjective.class);
        this.objectiveCard = new ObjectiveCard("test", CardVisibility.PUBLIC, "titleTest",
                "descriptionTest", obj);
    }
    @Test
    void getCardId() {
        Assertions.assertEquals("test", objectiveCard.getCardId());
    }

    @Test
    void getVisibility() {
        Assertions.assertEquals(CardVisibility.PUBLIC, objectiveCard.getVisibility());
    }

    @Test
    void getObjectiveTest() {
        Assertions.assertEquals(obj, objectiveCard.getObjective());
    }

    @Test
    void setCardId() {
        this.objectiveCard.setCardId("testName");
        Assertions.assertSame("testName", this.objectiveCard.getCardId());
    }

    @Test
    void setVisibility() {
        this.objectiveCard.setVisibility(CardVisibility.PRIVATE);
        Assertions.assertEquals(CardVisibility.PRIVATE, this.objectiveCard.getVisibility());

    }

    @Test
    void setObjective() {
        IObjective obj = mock(IObjective.class);
        this.objectiveCard.setObjective(obj);
        Assertions.assertSame(obj, this.objectiveCard.getObjective());
    }
}
