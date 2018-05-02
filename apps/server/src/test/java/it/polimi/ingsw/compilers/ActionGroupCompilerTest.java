package it.polimi.ingsw.compilers;

import it.polimi.ingsw.compilers.utils.ActionParameter;
import it.polimi.ingsw.core.actions.*;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.utils.IterableRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

class ActionGroupCompilerTest {

    @Test
    void testEmptyGroup() throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException {
        ActionGroupCompiler compiler = new ActionGroupCompiler();

        String xmlActionGroup =
                "<action-group id=\"empty\" root=\"none\">" +
                "</action-group>";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlActionGroup));
        Document doc = builder.parse(is);

        CompiledActionGroup compiledActionGroup = compiler.compile(doc.getDocumentElement(), null);

        Assertions.assertEquals("none", compiledActionGroup.getRootActionId());
        Assertions.assertEquals(ActionGroup.class, compiledActionGroup.getClassToInstantiate());
        Assertions.assertEquals(IterableRange.unitaryInteger(), compiledActionGroup.getChooseBetween());
        Assertions.assertEquals(IterableRange.unitaryInteger(), compiledActionGroup.getRepetitions());

        ActionData actionData = compiledActionGroup.getActionData();

        Assertions.assertEquals("empty", actionData.getId());
        Assertions.assertNull(actionData.getNextActionId());
        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertNull(actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        Assertions.assertEquals(0, compiledActionGroup.getActions().size());
    }

    @Test
    void testGroupWithFixedRepetitions() throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException {
        ActionGroupCompiler compiler = new ActionGroupCompiler();

        String xmlActionGroup =
                "<action-group id=\"empty\" root=\"none\" repetitions=\"4\">" +
                "</action-group>";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlActionGroup));
        Document doc = builder.parse(is);

        CompiledActionGroup compiledActionGroup = compiler.compile(doc.getDocumentElement(), null);

        Assertions.assertEquals("none", compiledActionGroup.getRootActionId());
        Assertions.assertEquals(ActionGroup.class, compiledActionGroup.getClassToInstantiate());
        Assertions.assertEquals(IterableRange.unitaryInteger(), compiledActionGroup.getChooseBetween());
        Assertions.assertEquals(4, compiledActionGroup.getRepetitions().getStart().intValue());
        Assertions.assertEquals(4, compiledActionGroup.getRepetitions().getEnd().intValue());

        ActionData actionData = compiledActionGroup.getActionData();

        Assertions.assertEquals("empty", actionData.getId());
        Assertions.assertNull(actionData.getNextActionId());
        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertNull(actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        Assertions.assertEquals(0, compiledActionGroup.getActions().size());
    }

    @Test
    void testGroupWithRangedRepetitions() throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException {
        ActionGroupCompiler compiler = new ActionGroupCompiler();

        String xmlActionGroup =
                "<action-group id=\"empty\" root=\"none\" repetitions=\"2..6\">" +
                "</action-group>";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlActionGroup));
        Document doc = builder.parse(is);

        CompiledActionGroup compiledActionGroup = compiler.compile(doc.getDocumentElement(), null);

        Assertions.assertEquals("none", compiledActionGroup.getRootActionId());
        Assertions.assertEquals(ActionGroup.class, compiledActionGroup.getClassToInstantiate());
        Assertions.assertEquals(IterableRange.unitaryInteger(), compiledActionGroup.getChooseBetween());
        Assertions.assertEquals(2, compiledActionGroup.getRepetitions().getStart().intValue());
        Assertions.assertEquals(6, compiledActionGroup.getRepetitions().getEnd().intValue());

        ActionData actionData = compiledActionGroup.getActionData();

        Assertions.assertEquals("empty", actionData.getId());
        Assertions.assertNull(actionData.getNextActionId());
        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertNull(actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        Assertions.assertEquals(0, compiledActionGroup.getActions().size());
    }

    @Test
    void testGroupWithFixedChooseBetween() throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException {
        ActionGroupCompiler compiler = new ActionGroupCompiler();

        String xmlActionGroup =
                "<action-group id=\"empty\" root=\"none\" chooseBetween=\"4\">" +
                "</action-group>";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlActionGroup));
        Document doc = builder.parse(is);

        CompiledActionGroup compiledActionGroup = compiler.compile(doc.getDocumentElement(), null);

        Assertions.assertEquals("none", compiledActionGroup.getRootActionId());
        Assertions.assertEquals(ActionGroup.class, compiledActionGroup.getClassToInstantiate());
        Assertions.assertEquals(IterableRange.unitaryInteger(), compiledActionGroup.getRepetitions());
        Assertions.assertEquals(4, compiledActionGroup.getChooseBetween().getStart().intValue());
        Assertions.assertEquals(4, compiledActionGroup.getChooseBetween().getEnd().intValue());

        ActionData actionData = compiledActionGroup.getActionData();

        Assertions.assertEquals("empty", actionData.getId());
        Assertions.assertNull(actionData.getNextActionId());
        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertNull(actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        Assertions.assertEquals(0, compiledActionGroup.getActions().size());
    }

    @Test
    void testGroupWithRangedChooseBetween() throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException {
        ActionGroupCompiler compiler = new ActionGroupCompiler();

        String xmlActionGroup =
                "<action-group id=\"empty\" root=\"none\" chooseBetween=\"2..6\">" +
                "</action-group>";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlActionGroup));
        Document doc = builder.parse(is);

        CompiledActionGroup compiledActionGroup = compiler.compile(doc.getDocumentElement(), null);

        Assertions.assertEquals("none", compiledActionGroup.getRootActionId());
        Assertions.assertEquals(ActionGroup.class, compiledActionGroup.getClassToInstantiate());
        Assertions.assertEquals(IterableRange.unitaryInteger(), compiledActionGroup.getRepetitions());
        Assertions.assertEquals(2, compiledActionGroup.getChooseBetween().getStart().intValue());
        Assertions.assertEquals(6, compiledActionGroup.getChooseBetween().getEnd().intValue());

        ActionData actionData = compiledActionGroup.getActionData();

        Assertions.assertEquals("empty", actionData.getId());
        Assertions.assertNull(actionData.getNextActionId());
        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertNull(actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        Assertions.assertEquals(0, compiledActionGroup.getActions().size());
    }

    @Test
    void testSimpleGroup() throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException {
        ActionGroupCompiler compiler = new ActionGroupCompiler();

        String xmlActionGroup =
                "<action-group id=\"inc_dec\" root=\"inc\">" +
                        "   <action id=\"inc\" effect=\"increment $DIE$ 1\" />" +
                        "   <action id=\"dec\" effect=\"decrement $DIE$ 1\" />" +
                        "</action-group>";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlActionGroup));
        Document doc = builder.parse(is);

        CompiledActionGroup compiledActionGroup = compiler.compile(doc.getDocumentElement(), null);

        Assertions.assertEquals("inc", compiledActionGroup.getRootActionId());
        Assertions.assertEquals(ActionGroup.class, compiledActionGroup.getClassToInstantiate());
        Assertions.assertEquals(IterableRange.unitaryInteger(), compiledActionGroup.getChooseBetween());
        Assertions.assertEquals(IterableRange.unitaryInteger(), compiledActionGroup.getRepetitions());

        ActionData actionData = compiledActionGroup.getActionData();

        Assertions.assertEquals("inc_dec", actionData.getId());
        Assertions.assertNull(actionData.getNextActionId());
        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertNull(actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        List<CompiledExecutableAction> innerActions = compiledActionGroup.getActions();

        innerActions.forEach(action -> Assertions.assertEquals(action.getClass(), CompiledAction.class));

        CompiledAction firstAction = (CompiledAction) innerActions.get(0);

        Assertions.assertEquals("increment", firstAction.getActionId());
        Assertions.assertEquals(IncrementAction.class, firstAction.getClassToInstantiate());
        Assertions.assertEquals(false, firstAction.getRequiresUserInteraction());

        ActionData firstActionData = firstAction.getActionData();

        Assertions.assertEquals("inc", firstActionData.getId());
        Assertions.assertNull(firstActionData.getNextActionId());
        Assertions.assertNull(firstActionData.getDescriptionKey());
        Assertions.assertNull(firstActionData.getResultIdentifier());
        Assertions.assertNull(firstActionData.getConstraint());

        ActionParameter[] firstActionParameters = firstAction.getParameters();
        Assertions.assertEquals(2, firstActionParameters.length);

        Assertions.assertSame(0, firstActionParameters[0].getPosition());
        Assertions.assertEquals(Die.class, firstActionParameters[0].getType());
        Assertions.assertEquals("DIE", firstActionParameters[0].getValue().getRawValue());
        Assertions.assertTrue(firstActionParameters[0].getValue().needsToBeComputed());

        Assertions.assertSame(1, firstActionParameters[1].getPosition());
        Assertions.assertEquals(Integer.class, firstActionParameters[1].getType());
        Assertions.assertEquals("1", firstActionParameters[1].getValue().getRawValue());
        Assertions.assertFalse(firstActionParameters[1].getValue().needsToBeComputed());

        CompiledAction secondAction = (CompiledAction) innerActions.get(1);

        Assertions.assertEquals("decrement", secondAction.getActionId());
        Assertions.assertEquals(DecrementAction.class, secondAction.getClassToInstantiate());
        Assertions.assertEquals(false, secondAction.getRequiresUserInteraction());

        ActionData secondActionData = secondAction.getActionData();

        Assertions.assertEquals("dec", secondActionData.getId());
        Assertions.assertNull(secondActionData.getNextActionId());
        Assertions.assertNull(secondActionData.getDescriptionKey());
        Assertions.assertNull(secondActionData.getResultIdentifier());
        Assertions.assertNull(secondActionData.getConstraint());

        ActionParameter[] secondActionParameters = firstAction.getParameters();
        Assertions.assertEquals(2, secondActionParameters.length);

        Assertions.assertSame(0, secondActionParameters[0].getPosition());
        Assertions.assertEquals(Die.class, secondActionParameters[0].getType());
        Assertions.assertEquals("DIE", secondActionParameters[0].getValue().getRawValue());
        Assertions.assertTrue(secondActionParameters[0].getValue().needsToBeComputed());

        Assertions.assertSame(1, secondActionParameters[1].getPosition());
        Assertions.assertEquals(Integer.class, secondActionParameters[1].getType());
        Assertions.assertEquals("1", secondActionParameters[1].getValue().getRawValue());
        Assertions.assertFalse(secondActionParameters[1].getValue().needsToBeComputed());
    }

    @Test
    void testComplexGroup() throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException {
        ActionGroupCompiler compiler = new ActionGroupCompiler();

        String xmlActionGroup =
                "<action-group id=\"inc_dec\" root=\"start\">\n" +
                "    <action id=\"start\" effect=\"increment $DIE$ 1\" next=\"dec\" />\n" +
                "    <action id=\"dec\" effect=\"decrement $DIE$ 1\" next=\"ch_2\" />\n" +
                "    <action-group id=\"ch_2\" chooseBetween=\"2\" next=\"rep_ch\" root=\"start\" >\n" +
                "        <action id=\"start\" effect=\"choose_position $window$\" result=\"POS\" next=\"pick\"/>\n" +
                "        <action id=\"pick\" effect=\"pick_at $window$ $POS$\" result=\"DIE\" next=\"loop\"/>\n" +
                "        <action-group id=\"loop\" repetitions=\"1..2\" root=\"choose\">\n" +
                "            <action id=\"choose\" effect=\"choose_die $window$ [color=$DIE_COLOR$]\" result=\"DIE\" next=\"pick\"/>\n" +
                "            <action id=\"pick\" effect=\"pick_die $window$ $DIE$\" result=\"DIE\" next=\"choose_position\"/>\n" +
                "            <action id=\"choose_position\" effect=\"choose_position_for_die $window$ $DIE$\" result=\"POS\" next=\"place\" />\n" +
                "            <action id=\"place\" effect=\"place $DIE$ $window$ $POS$\"/>\n" +
                "        </action-group>\n" +
                "    </action-group>\n" +
                "    <action-group id=\"rep_ch\" repetitions=\"0..4\" chooseBetween=\"1..7\" next=\"choose_position\">\n" +
                "        <action id=\"start\" effect=\"choose_die $draft_pool$\" result=\"DIE\" />\n" +
                "        <action id=\"flip\" effect=\"flip $DIE$\" />\n" +
                "    </action-group>\n" +
                "    <action id=\"choose_position\" effect=\"choose_position_for_die $window$ $DIE$ [ignore_color=true]\" result=\"POS\" next=\"place\" />\n" +
                "    <action id=\"place\" effect=\"place $DIE$ $window$ $POS$\"/>\n" +
                "</action-group>";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlActionGroup));
        Document doc = builder.parse(is);

        CompiledActionGroup compiledActionGroup = compiler.compile(doc.getDocumentElement(), null);

        Assertions.assertEquals("start", compiledActionGroup.getRootActionId());
        Assertions.assertEquals(ActionGroup.class, compiledActionGroup.getClassToInstantiate());
        Assertions.assertEquals(IterableRange.unitaryInteger(), compiledActionGroup.getChooseBetween());
        Assertions.assertEquals(IterableRange.unitaryInteger(), compiledActionGroup.getRepetitions());

        ActionData actionData = compiledActionGroup.getActionData();

        Assertions.assertEquals("inc_dec", actionData.getId());
        Assertions.assertNull(actionData.getNextActionId());
        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertNull(actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        List<CompiledExecutableAction> innerActions = compiledActionGroup.getActions();
        Assertions.assertEquals(6, innerActions.size());

        Assertions.assertEquals(CompiledAction.class, innerActions.get(0).getClass());
        Assertions.assertEquals(IncrementAction.class, innerActions.get(0).getClassToInstantiate());

        Assertions.assertEquals(CompiledAction.class, innerActions.get(1).getClass());
        Assertions.assertEquals(DecrementAction.class, innerActions.get(1).getClassToInstantiate());

        Assertions.assertEquals(CompiledActionGroup.class, innerActions.get(2).getClass());
        Assertions.assertEquals(ActionGroup.class, innerActions.get(2).getClassToInstantiate());

        CompiledActionGroup firstInnerActionGroup = (CompiledActionGroup) innerActions.get(2);

        Assertions.assertEquals(IterableRange.unitaryInteger(), firstInnerActionGroup.getRepetitions());
        Assertions.assertEquals(2, firstInnerActionGroup.getChooseBetween().getStart().intValue());
        Assertions.assertEquals(2, firstInnerActionGroup.getChooseBetween().getEnd().intValue());

        List<CompiledExecutableAction> firstInnerInnerActions = firstInnerActionGroup.getActions();
        Assertions.assertEquals(3, firstInnerInnerActions.size());

        Assertions.assertEquals(CompiledAction.class, firstInnerInnerActions.get(0).getClass());
        Assertions.assertEquals(ChoosePositionAction.class, firstInnerInnerActions.get(0).getClassToInstantiate());

        Assertions.assertEquals(CompiledAction.class, firstInnerInnerActions.get(1).getClass());
        Assertions.assertEquals(PickAtAction.class, firstInnerInnerActions.get(1).getClassToInstantiate());

        Assertions.assertEquals(CompiledActionGroup.class, firstInnerInnerActions.get(2).getClass());
        Assertions.assertEquals(ActionGroup.class, firstInnerInnerActions.get(2).getClassToInstantiate());

        CompiledActionGroup firstInnerInnerInnerActionGroup = (CompiledActionGroup) firstInnerInnerActions.get(2);

        Assertions.assertEquals(IterableRange.unitaryInteger(), firstInnerInnerInnerActionGroup.getChooseBetween());
        Assertions.assertEquals(1, firstInnerInnerInnerActionGroup.getRepetitions().getStart().intValue());
        Assertions.assertEquals(2, firstInnerInnerInnerActionGroup.getRepetitions().getEnd().intValue());

        List<CompiledExecutableAction> firstInnerInnerInnerActions = firstInnerInnerInnerActionGroup.getActions();
        Assertions.assertEquals(4, firstInnerInnerInnerActions.size());

        Assertions.assertEquals(CompiledAction.class, firstInnerInnerInnerActions.get(0).getClass());
        Assertions.assertEquals(ChooseDieAction.class, firstInnerInnerInnerActions.get(0).getClassToInstantiate());

        Assertions.assertEquals(CompiledAction.class, firstInnerInnerInnerActions.get(1).getClass());
        Assertions.assertEquals(PickDieAction.class, firstInnerInnerInnerActions.get(1).getClassToInstantiate());

        Assertions.assertEquals(CompiledAction.class, firstInnerInnerInnerActions.get(2).getClass());
        Assertions.assertEquals(ChoosePositionForDieAction.class, firstInnerInnerInnerActions.get(2).getClassToInstantiate());

        Assertions.assertEquals(CompiledAction.class, firstInnerInnerInnerActions.get(3).getClass());
        Assertions.assertEquals(PlaceAction.class, firstInnerInnerInnerActions.get(3).getClassToInstantiate());

        CompiledActionGroup secondInnerActionGroup = (CompiledActionGroup) innerActions.get(3);

        Assertions.assertEquals(1, secondInnerActionGroup.getChooseBetween().getStart().intValue());
        Assertions.assertEquals(7, secondInnerActionGroup.getChooseBetween().getEnd().intValue());
        Assertions.assertEquals(0, secondInnerActionGroup.getRepetitions().getStart().intValue());
        Assertions.assertEquals(4, secondInnerActionGroup.getRepetitions().getEnd().intValue());

        List<CompiledExecutableAction> secondInnerInnerActions = secondInnerActionGroup.getActions();
        Assertions.assertEquals(2, secondInnerInnerActions.size());

        Assertions.assertEquals(CompiledAction.class, secondInnerInnerActions.get(0).getClass());
        Assertions.assertEquals(ChooseDieAction.class, secondInnerInnerActions.get(0).getClassToInstantiate());

        Assertions.assertEquals(CompiledAction.class, secondInnerInnerActions.get(1).getClass());
        Assertions.assertEquals(FlipAction.class, secondInnerInnerActions.get(1).getClassToInstantiate());

        Assertions.assertEquals(CompiledAction.class, innerActions.get(4).getClass());
        Assertions.assertEquals(ChoosePositionForDieAction.class, innerActions.get(4).getClassToInstantiate());

        Assertions.assertEquals(CompiledAction.class, innerActions.get(5).getClass());
        Assertions.assertEquals(PlaceAction.class, innerActions.get(5).getClassToInstantiate());
    }
}