package it.polimi.ingsw.server.compilers.instructions.predicates.directives;

import it.polimi.ingsw.server.compilers.actions.directives.ActionDirective;
import it.polimi.ingsw.server.compilers.actions.directives.ActionDirectivesCompiler;
import it.polimi.ingsw.server.compilers.commons.directives.ParameterDirective;
import it.polimi.ingsw.server.instructions.predicates.DistinctColorPredicate;
import it.polimi.ingsw.server.instructions.predicates.DistinctShadePredicate;
import it.polimi.ingsw.server.utils.VariableSupplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

class PredicateDirectivesCompilerTest {

    @Test
    void testSuccessfulCompilation() throws ClassNotFoundException, SAXException, ParserConfigurationException, IOException {
        List<PredicateDirective> directives = PredicateDirectivesCompiler.compile();

        Assertions.assertEquals(2, directives.size());

        PredicateDirective distinctShade = directives.get(0);
        Assertions.assertNotNull(distinctShade);
        Assertions.assertEquals(DistinctShadePredicate.class, distinctShade.getTargetClass());
        Assertions.assertEquals("distinct_shade", distinctShade.getId());

        List<ParameterDirective> distinctShadeParameters = distinctShade.getParametersDirectives();
        testDistinctParameters(distinctShadeParameters);

        PredicateDirective distinctColor = directives.get(1);
        Assertions.assertNotNull(distinctColor);
        Assertions.assertEquals(DistinctColorPredicate.class, distinctColor.getTargetClass());
        Assertions.assertEquals("distinct_color", distinctColor.getId());

        List<ParameterDirective> distinctColorParameters = distinctColor.getParametersDirectives();
        testDistinctParameters(distinctColorParameters);
    }

    @Test
    void testClassNotFoundExceptionOnAction() {
        Assertions.assertThrows(
                ClassNotFoundException.class,
                () -> PredicateDirectivesCompiler.compile(
                        "directives/predicates-directives-class-not-found.xml",
                        true
                )
        );
    }

    @Test
    void testClassNotFoundExceptionOnParameter() {
        Assertions.assertThrows(
                ClassNotFoundException.class,
                () -> PredicateDirectivesCompiler.compile(
                        "directives/predicates-directives-class-not-found2.xml",
                        true
                )
        );
    }

    @Test
    void testEmptyFile() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        List<ActionDirective> directives = ActionDirectivesCompiler.compile("directives/predicates-directives-empty.xml", true);

        Assertions.assertTrue(directives.isEmpty());
    }

    private void testDistinctParameters(List<ParameterDirective> distinctParameters) {
        Assertions.assertEquals(1, distinctParameters.size());

        ParameterDirective parameterDirective = distinctParameters.get(0);
        Assertions.assertEquals(VariableSupplier.class, parameterDirective.getParameterType());
        Assertions.assertEquals(0, parameterDirective.getPosition().intValue());
        Assertions.assertTrue(parameterDirective.isMultiple());
        Assertions.assertFalse(parameterDirective.isOptional());
        Assertions.assertNull(parameterDirective.getDefaultValue());
        Assertions.assertNull(parameterDirective.getName());
    }
}