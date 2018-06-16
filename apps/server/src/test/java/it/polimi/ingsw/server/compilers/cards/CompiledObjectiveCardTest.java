package it.polimi.ingsw.server.compilers.cards;

import it.polimi.ingsw.core.CardVisibility;
import it.polimi.ingsw.server.compilers.objectives.CompiledObjective;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CompiledObjectiveCardTest {

    @Test
    void testGetters() {
        CompiledObjective compiledObjective = mock(CompiledObjective.class);

        CompiledObjectiveCard compiledObjectiveCard = new CompiledObjectiveCard(
                "card_id",
                "card_name",
                compiledObjective,
                CardVisibility.PUBLIC
        );

        Assertions.assertEquals("card_id", compiledObjectiveCard.getId());
        Assertions.assertEquals("card_name", compiledObjectiveCard.getName());
        Assertions.assertSame(compiledObjective, compiledObjectiveCard.getObjective());
        Assertions.assertEquals(CardVisibility.PUBLIC, compiledObjectiveCard.getVisibility());
    }

    @Test
    void testGettersFromCard() {
        CompiledObjective compiledObjective = mock(CompiledObjective.class);
        CompiledCard compiledCard = mock(CompiledCard.class);
        when(compiledCard.getId()).thenReturn("card_id");
        when(compiledCard.getName()).thenReturn("card_name");

        CompiledObjectiveCard compiledObjectiveCard = new CompiledObjectiveCard(
                compiledCard,
                compiledObjective,
                CardVisibility.PUBLIC
        );

        Assertions.assertEquals("card_id", compiledObjectiveCard.getId());
        Assertions.assertEquals("card_name", compiledObjectiveCard.getName());
        Assertions.assertSame(compiledObjective, compiledObjectiveCard.getObjective());
        Assertions.assertEquals(CardVisibility.PUBLIC, compiledObjectiveCard.getVisibility());
    }
}