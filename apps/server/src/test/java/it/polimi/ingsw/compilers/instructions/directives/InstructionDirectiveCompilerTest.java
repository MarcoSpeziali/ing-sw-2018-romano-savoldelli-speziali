package it.polimi.ingsw.compilers.instructions.directives;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.instructions.*;
import it.polimi.ingsw.models.Die;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

class InstructionDirectiveCompilerTest {

    @Test
    void testSuccessfulCompilation() throws ClassNotFoundException, SAXException, ParserConfigurationException, IOException {
        List<InstructionDirective> directives = InstructionDirectiveCompiler.compile();

        Assertions.assertEquals(5, directives.size());

        InstructionDirective forDie = directives.get(0);
        Assertions.assertEquals("for-die", forDie.getId());
        Assertions.assertEquals(ForDieInstruction.class, forDie.getTargetClass());
        Assertions.assertNotNull(forDie.getParameterDirectives());
        Assertions.assertFalse(forDie.getParameterDirectives().isEmpty());
        Assertions.assertNotNull(forDie.getExposedVariableDirectives());
        Assertions.assertFalse(forDie.getExposedVariableDirectives().isEmpty());
        testForDieParameters(forDie.getParameterDirectives());
        testForDieExposedVariables(forDie.getExposedVariableDirectives());

        InstructionDirective forRow = directives.get(1);
        Assertions.assertEquals("for-row", forRow.getId());
        Assertions.assertEquals(ForRowInstruction.class, forRow.getTargetClass());
        Assertions.assertNotNull(forRow.getParameterDirectives());
        Assertions.assertTrue(forRow.getParameterDirectives().isEmpty());
        Assertions.assertNotNull(forRow.getExposedVariableDirectives());
        Assertions.assertFalse(forRow.getExposedVariableDirectives().isEmpty());
        testForRowExposedVariables(forRow.getExposedVariableDirectives());

        InstructionDirective takeIf = directives.get(2);
        Assertions.assertEquals("take-if", takeIf.getId());
        Assertions.assertEquals(TakeIfInstruction.class, takeIf.getTargetClass());
        Assertions.assertNotNull(takeIf.getParameterDirectives());
        Assertions.assertFalse(takeIf.getParameterDirectives().isEmpty());
        Assertions.assertNotNull(takeIf.getExposedVariableDirectives());
        Assertions.assertTrue(takeIf.getExposedVariableDirectives().isEmpty());
        testTakeIfParameters(takeIf.getParameterDirectives());

        InstructionDirective takeMin = directives.get(3);
        Assertions.assertEquals("take-min", takeMin.getId());
        Assertions.assertEquals(TakeMinInstruction.class, takeMin.getTargetClass());
        Assertions.assertNotNull(takeMin.getParameterDirectives());
        Assertions.assertTrue(takeMin.getParameterDirectives().isEmpty());
        Assertions.assertNotNull(takeMin.getExposedVariableDirectives());
        Assertions.assertTrue(takeMin.getExposedVariableDirectives().isEmpty());

        InstructionDirective countDiagonal = directives.get(4);
        Assertions.assertEquals("count-diagonal", countDiagonal.getId());
        Assertions.assertEquals(CountDiagonalInstruction.class, countDiagonal.getTargetClass());
        Assertions.assertNotNull(countDiagonal.getParameterDirectives());
        Assertions.assertFalse(countDiagonal.getParameterDirectives().isEmpty());
        Assertions.assertNotNull(countDiagonal.getExposedVariableDirectives());
        Assertions.assertTrue(countDiagonal.getExposedVariableDirectives().isEmpty());

        testCountDiagonalParameters(countDiagonal.getParameterDirectives());
    }

    @Test
    void testClassNotFoundExceptionOnAction() {
        Assertions.assertThrows(
                ClassNotFoundException.class,
                () -> InstructionDirectiveCompiler.compile(
                        "directives/instructions-directives-class-not-found.xml",
                        true
                )
        );
    }

    @Test
    void testClassNotFoundExceptionOnParameter() {
        Assertions.assertThrows(
                ClassNotFoundException.class,
                () -> InstructionDirectiveCompiler.compile(
                        "directives/instructions-directives-class-not-found2.xml",
                        true
                )
        );
    }

    @Test
    void testEmptyFile() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        List<InstructionDirective> directives = InstructionDirectiveCompiler.compile("directives/instructions-directives-empty.xml", true);

        Assertions.assertTrue(directives.isEmpty());
    }

    private void testForDieParameters(List<InstructionParameterDirective> parameterDirectives) {
        Assertions.assertEquals(2, parameterDirectives.size());

        InstructionParameterDirective firstDirective = parameterDirectives.get(0);
        Assertions.assertEquals(Integer.class, firstDirective.getParameterType());
        Assertions.assertEquals("filterShade", firstDirective.getName());
        Assertions.assertEquals(0, firstDirective.getDefaultValue());
        Assertions.assertEquals(0, firstDirective.getPosition().intValue());
        Assertions.assertTrue(firstDirective.isOptional());
        Assertions.assertFalse(firstDirective.isPredicate());

        InstructionParameterDirective secondDirective = parameterDirectives.get(1);
        Assertions.assertEquals(GlassColor.class, secondDirective.getParameterType());
        Assertions.assertEquals("filterColor", secondDirective.getName());
        Assertions.assertNull(secondDirective.getDefaultValue());
        Assertions.assertEquals(1, secondDirective.getPosition().intValue());
        Assertions.assertTrue(secondDirective.isOptional());
        Assertions.assertFalse(secondDirective.isPredicate());
    }

    private void testForDieExposedVariables(List<InstructionExposedVariableDirective> exposedVariableDirectives) {
        Assertions.assertEquals(1, exposedVariableDirectives.size());

        InstructionExposedVariableDirective exposedVariableDirective = exposedVariableDirectives.get(0);
        Assertions.assertEquals("die", exposedVariableDirective.getName());
        Assertions.assertEquals(Die.class, exposedVariableDirective.getVariableType());
    }

    private void testForRowExposedVariables(List<InstructionExposedVariableDirective> exposedVariableDirectives) {
        Assertions.assertEquals(1, exposedVariableDirectives.size());

        InstructionExposedVariableDirective exposedVariableDirective = exposedVariableDirectives.get(0);
        Assertions.assertEquals("row", exposedVariableDirective.getName());
        Assertions.assertEquals(Die[].class, exposedVariableDirective.getVariableType());
    }

    private void testTakeIfParameters(List<InstructionParameterDirective> parameterDirectives) {
        Assertions.assertEquals(1, parameterDirectives.size());

        InstructionParameterDirective firstDirective = parameterDirectives.get(0);
        Assertions.assertNull(firstDirective.getParameterType());
        Assertions.assertEquals("predicate", firstDirective.getName());
        Assertions.assertNull(firstDirective.getDefaultValue());
        Assertions.assertEquals(0, firstDirective.getPosition().intValue());
        Assertions.assertFalse(firstDirective.isOptional());
        Assertions.assertTrue(firstDirective.isPredicate());
    }

    private void testCountDiagonalParameters(List<InstructionParameterDirective> parameterDirectives) {
        Assertions.assertEquals(3, parameterDirectives.size());

        InstructionParameterDirective firstDirective = parameterDirectives.get(0);
        Assertions.assertEquals(DieFilter.class, firstDirective.getParameterType());
        Assertions.assertEquals("filter", firstDirective.getName());
        Assertions.assertNull(firstDirective.getDefaultValue());
        Assertions.assertEquals(0, firstDirective.getPosition().intValue());
        Assertions.assertFalse(firstDirective.isOptional());
        Assertions.assertFalse(firstDirective.isPredicate());

        InstructionParameterDirective secondDirective = parameterDirectives.get(1);
        Assertions.assertEquals(Integer.class, secondDirective.getParameterType());
        Assertions.assertEquals("strictShade", secondDirective.getName());
        Assertions.assertEquals(0, secondDirective.getDefaultValue());
        Assertions.assertEquals(1, secondDirective.getPosition().intValue());
        Assertions.assertTrue(secondDirective.isOptional());
        Assertions.assertFalse(secondDirective.isPredicate());

        InstructionParameterDirective thirdDirective = parameterDirectives.get(2);
        Assertions.assertEquals(GlassColor.class, thirdDirective.getParameterType());
        Assertions.assertEquals("strictColor", thirdDirective.getName());
        Assertions.assertNull(thirdDirective.getDefaultValue());
        Assertions.assertEquals(2, thirdDirective.getPosition().intValue());
        Assertions.assertTrue(thirdDirective.isOptional());
        Assertions.assertFalse(thirdDirective.isPredicate());
    }
}