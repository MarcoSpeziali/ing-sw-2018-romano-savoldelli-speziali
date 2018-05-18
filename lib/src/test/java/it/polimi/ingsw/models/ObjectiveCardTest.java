package it.polimi.ingsw.models;

import it.polimi.ingsw.core.CardVisibility;
import it.polimi.ingsw.core.Objective;
import it.polimi.ingsw.utils.text.LocalizedString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.Visibility;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;

class ObjectiveCardTest {

    private ObjectiveCard objectiveCard;
    private Objective obj;

    @BeforeEach
    void setUp() {
        this.obj = new Objective();
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
        Objective obj = mock(Objective.class);
        this.objectiveCard.setObjective(obj);
        Assertions.assertSame(obj, this.objectiveCard.getObjective());
    }
}
