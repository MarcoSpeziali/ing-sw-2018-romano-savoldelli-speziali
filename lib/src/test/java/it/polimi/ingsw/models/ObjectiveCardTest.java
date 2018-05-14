package it.polimi.ingsw.models;

import it.polimi.ingsw.core.CardVisibility;
import it.polimi.ingsw.core.Objective;
import it.polimi.ingsw.utils.text.LocalizedString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;

class ObjectiveCardTest {

    private ObjectiveCard objectiveCard;

    @BeforeEach
    void setUp() {
        this.objectiveCard = new ObjectiveCard("test", CardVisibility.PUBLIC, 3,
                mock(Image.class),mock(Image.class), mock(LocalizedString.class), mock(LocalizedString.class));
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
    void getPointsPerCompletion() {
        Assertions.assertEquals(3, objectiveCard.getPointsPerCompletion());
    }

    @Test
    void getObjectiveTest() {
        Assertions.assertEquals(mock(Objective.class), objectiveCard.getObjective());
    }
}
