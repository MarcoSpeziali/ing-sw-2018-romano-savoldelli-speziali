package it.polimi.ingsw.server.compilers.actions.directives;

import it.polimi.ingsw.server.actions.*;
import it.polimi.ingsw.server.compilers.commons.directives.ParameterDirective;
import it.polimi.ingsw.server.utils.VariableSupplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

class ActionDirectivesCompilerTest {

    @Test
    void testSuccessfulCompilation() throws ClassNotFoundException, SAXException, ParserConfigurationException, IOException {
        List<ActionDirective> directives = ActionDirectivesCompiler.compile();

        Assertions.assertEquals(3, directives.size());

        ActionDirective actionDirective1 = directives.get(0);
        Assertions.assertNotNull(actionDirective1);
        Assertions.assertEquals(ChoosePositionAction.class, actionDirective1.getTargetClass());
        Assertions.assertEquals("choose_position", actionDirective1.getId());

        List<ParameterDirective> chooseDieParameters = actionDirective1.getParametersDirectives();
        testChooseDieParameters(chooseDieParameters);

        ActionDirective choosePosition = directives.get(1);
        Assertions.assertNotNull(choosePosition);
        Assertions.assertEquals(ChoosePositionAction.class, choosePosition.getTargetClass());
        Assertions.assertEquals("choose_position", choosePosition.getId());

        List<ParameterDirective> choosePositionParameters = choosePosition.getParametersDirectives();
        testChoosePositionParameters(choosePositionParameters);

        ActionDirective choosePositionForDie = directives.get(2);
        Assertions.assertNotNull(choosePositionForDie);
        Assertions.assertEquals(ChoosePositionForDieAction.class, choosePositionForDie.getTargetClass());
        Assertions.assertEquals("choose_position_for_die", choosePositionForDie.getId());

        List<ParameterDirective> choosePositionForDieParameters = choosePositionForDie.getParametersDirectives();
        testChoosePositionForDieParameters(choosePositionForDieParameters);
    }

    @Test
    void testClassNotFoundExceptionOnAction() {
        Assertions.assertThrows(ClassNotFoundException.class, () -> ActionDirectivesCompiler.compile("directives/actions-directives-class-not-found.xml", true));
    }

    @Test
    void testClassNotFoundExceptionOnParameter() {
        Assertions.assertThrows(ClassNotFoundException.class, () -> ActionDirectivesCompiler.compile("directives/actions-directives-class-not-found2.xml", true));
    }

    @Test
    void testEmptyFile() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        List<ActionDirective> directives = ActionDirectivesCompiler.compile("directives/actions-directives-empty.xml", true);

        Assertions.assertTrue(directives.isEmpty());
    }

    private void testChooseDieParameters(List<ParameterDirective> chooseDieParameters) {
        Assertions.assertNotNull(chooseDieParameters);
        Assertions.assertEquals(3, chooseDieParameters.size());

        ParameterDirective chooseLocation = chooseDieParameters.get(0);
        Assertions.assertNotNull(chooseLocation);
        Assertions.assertEquals(VariableSupplier.class, chooseLocation.getParameterType());
        Assertions.assertEquals(0, chooseLocation.getPosition().intValue());
        Assertions.assertFalse(chooseLocation.isOptional());
        Assertions.assertNull(chooseLocation.getName());
        Assertions.assertNull(chooseLocation.getDefaultValue());
        Assertions.assertFalse(chooseLocation.isMultiple());

        ParameterDirective glassColor = chooseDieParameters.get(1);
        Assertions.assertNotNull(glassColor);
        Assertions.assertEquals(VariableSupplier.class, glassColor.getParameterType());
        Assertions.assertEquals(1, glassColor.getPosition().intValue());
        Assertions.assertTrue(glassColor.isOptional());
        Assertions.assertEquals("color", glassColor.getName());
        Assertions.assertNull(glassColor.getDefaultValue());
        Assertions.assertFalse(glassColor.isMultiple());

        ParameterDirective shade = chooseDieParameters.get(2);
        Assertions.assertNotNull(shade);
        Assertions.assertEquals(VariableSupplier.class, shade.getParameterType());
        Assertions.assertEquals(2, shade.getPosition().intValue());
        Assertions.assertTrue(shade.isOptional());
        Assertions.assertEquals("shade", shade.getName());
        Assertions.assertEquals(0, shade.getDefaultValue());
        Assertions.assertFalse(shade.isMultiple());
    }

    private void testChoosePositionParameters(List<ParameterDirective> choosePositionParameters) {
        Assertions.assertNotNull(choosePositionParameters);
        Assertions.assertEquals(1, choosePositionParameters.size());

        ParameterDirective chooseLocation = choosePositionParameters.get(0);
        Assertions.assertNotNull(chooseLocation);
        Assertions.assertEquals(VariableSupplier.class, chooseLocation.getParameterType());
        Assertions.assertEquals(0, chooseLocation.getPosition().intValue());
        Assertions.assertFalse(chooseLocation.isOptional());
        Assertions.assertNull(chooseLocation.getName());
        Assertions.assertNull(chooseLocation.getDefaultValue());
        Assertions.assertFalse(chooseLocation.isMultiple());
    }

    private void testChoosePositionForDieParameters(List<ParameterDirective> choosePositionForDieParameters) {
        Assertions.assertNotNull(choosePositionForDieParameters);
        Assertions.assertEquals(5, choosePositionForDieParameters.size());

        ParameterDirective chooseLocation = choosePositionForDieParameters.get(0);
        Assertions.assertNotNull(chooseLocation);
        Assertions.assertEquals(VariableSupplier.class, chooseLocation.getParameterType());
        Assertions.assertEquals(0, chooseLocation.getPosition().intValue());
        Assertions.assertFalse(chooseLocation.isOptional());
        Assertions.assertNull(chooseLocation.getName());
        Assertions.assertNull(chooseLocation.getDefaultValue());
        Assertions.assertFalse(chooseLocation.isMultiple());

        ParameterDirective die = choosePositionForDieParameters.get(1);
        Assertions.assertNotNull(die);
        Assertions.assertEquals(VariableSupplier.class, die.getParameterType());
        Assertions.assertEquals(1, die.getPosition().intValue());
        Assertions.assertFalse(die.isOptional());
        Assertions.assertNull(die.getName());
        Assertions.assertNull(die.getDefaultValue());
        Assertions.assertFalse(die.isMultiple());

        ParameterDirective ignoreColor = choosePositionForDieParameters.get(2);
        Assertions.assertNotNull(ignoreColor);
        Assertions.assertEquals(Boolean.class, ignoreColor.getParameterType());
        Assertions.assertEquals(2, ignoreColor.getPosition().intValue());
        Assertions.assertTrue(ignoreColor.isOptional());
        Assertions.assertEquals("ignore_color", ignoreColor.getName());
        Assertions.assertFalse((Boolean) ignoreColor.getDefaultValue());
        Assertions.assertFalse(ignoreColor.isMultiple());

        ParameterDirective ignoreShade = choosePositionForDieParameters.get(3);
        Assertions.assertNotNull(ignoreShade);
        Assertions.assertEquals(Boolean.class, ignoreShade.getParameterType());
        Assertions.assertEquals(3, ignoreShade.getPosition().intValue());
        Assertions.assertTrue(ignoreShade.isOptional());
        Assertions.assertEquals("ignore_shade", ignoreShade.getName());
        Assertions.assertFalse((Boolean) ignoreShade.getDefaultValue());
        Assertions.assertFalse(ignoreShade.isMultiple());

        ParameterDirective ignoreAdjacency = choosePositionForDieParameters.get(4);
        Assertions.assertNotNull(ignoreAdjacency);
        Assertions.assertEquals(Boolean.class, ignoreAdjacency.getParameterType());
        Assertions.assertEquals(4, ignoreAdjacency.getPosition().intValue());
        Assertions.assertTrue(ignoreAdjacency.isOptional());
        Assertions.assertEquals("ignore_adjacency", ignoreAdjacency.getName());
        Assertions.assertFalse((Boolean) ignoreAdjacency.getDefaultValue());
        Assertions.assertFalse(ignoreAdjacency.isMultiple());
    }
}