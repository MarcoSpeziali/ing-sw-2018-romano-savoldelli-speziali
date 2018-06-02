package it.polimi.ingsw.server.compilers.cards;

import it.polimi.ingsw.server.compilers.actions.directives.ActionDirectivesCompiler;
import it.polimi.ingsw.server.compilers.instructions.directives.InstructionDirectiveCompiler;
import it.polimi.ingsw.server.compilers.instructions.predicates.directives.PredicateDirectivesCompiler;
import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class CardCompilerTest {

    @Test
    void compileToolCards() throws ClassNotFoundException, SAXException, ParserConfigurationException, IOException {
        List<CompiledToolCard> toolCards = CardCompiler.compileToolCards(
                ActionDirectivesCompiler.compile(
                        "directives/actions-directives-full.xml",
                        true
                )
        );

        Assertions.assertEquals(3, toolCards.size());

        CompiledToolCard grozingPliers = toolCards.get(0);
        CompiledToolCard fluxRemover = toolCards.get(1);
        CompiledToolCard tapWheel = toolCards.get(2);

        testGrozingPliers(grozingPliers);
        testFluxRemover(fluxRemover);
        testTapWheel(tapWheel);
    }

    @Test
    void compilePrivateObjectiveCards() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        List<CompiledObjectiveCard> toolCards = CardCompiler.compilePrivateObjectiveCards(
                InstructionDirectiveCompiler.compile("directives/instructions-directives-full.xml", true),
                PredicateDirectivesCompiler.compile()
        );

        Assertions.assertEquals(5, toolCards.size());

        String[] colors = Arrays.stream(GlassColor.values())
                .map(GlassColor::toString)
                .toArray(String[]::new);

        for (int i = 0; i < 5; i++) {
            CompiledObjectiveCard card = toolCards.get(i);

            Assertions.assertEquals(colors[i] + "_shade", card.getId());
            Assertions.assertEquals("cards.private_cards." + colors[i] + "_shade.name", card.getName());
        }
    }

    @Test
    void compilePublicObjectiveCards() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        List<CompiledObjectiveCard> toolCards = CardCompiler.compilePublicObjectiveCards(
                InstructionDirectiveCompiler.compile("directives/instructions-directives-full.xml", true),
                PredicateDirectivesCompiler.compile()
        );

        Assertions.assertEquals(4, toolCards.size());
    }

    void testGrozingPliers(CompiledToolCard grozingPliers) {
        Assertions.assertNotNull(grozingPliers);
        Assertions.assertEquals("grozing_pliers", grozingPliers.getId());
        Assertions.assertEquals("cards.tool_cards.grozing_pliers.name", grozingPliers.getName());
    }

    void testFluxRemover(CompiledToolCard fluxRemover) {
        Assertions.assertNotNull(fluxRemover);
        Assertions.assertEquals("flux_remover", fluxRemover.getId());
        Assertions.assertEquals("cards.tool_cards.flux_remover.name", fluxRemover.getName());
    }

    void testTapWheel(CompiledToolCard tapWheel) {
        Assertions.assertNotNull(tapWheel);
        Assertions.assertEquals("tap_wheel", tapWheel.getId());
        Assertions.assertEquals("cards.tool_cards.tap_wheel.name", tapWheel.getName());
    }
}