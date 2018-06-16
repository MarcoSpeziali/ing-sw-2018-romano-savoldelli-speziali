package it.polimi.ingsw.server.compilers.cards;

import it.polimi.ingsw.server.compilers.effects.CompiledEffect;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CompiledToolCardTest {

    @Test
    void testGetters() {
        CompiledEffect compiledEffect = mock(CompiledEffect.class);

        CompiledToolCard compiledToolCard = new CompiledToolCard(
                "card_id",
                "card_name",
                compiledEffect
        );

        Assertions.assertEquals("card_id", compiledToolCard.getId());
        Assertions.assertEquals("card_name", compiledToolCard.getName());
        Assertions.assertSame(compiledEffect, compiledToolCard.getEffect());
    }

    @Test
    void testGettersFromCard() {
        CompiledEffect compiledEffect = mock(CompiledEffect.class);
        CompiledCard compiledCard = mock(CompiledCard.class);
        when(compiledCard.getId()).thenReturn("card_id");
        when(compiledCard.getName()).thenReturn("card_name");

        CompiledToolCard compiledToolCard = new CompiledToolCard(
                compiledCard,
                compiledEffect
        );

        Assertions.assertEquals("card_id", compiledToolCard.getId());
        Assertions.assertEquals("card_name", compiledToolCard.getName());
        Assertions.assertSame(compiledEffect, compiledToolCard.getEffect());
    }
}