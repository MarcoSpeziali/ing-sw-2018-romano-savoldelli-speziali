package it.polimi.ingsw.compilers.actions;

import it.polimi.ingsw.compilers.actions.directives.ActionDirective;
import it.polimi.ingsw.compilers.actions.directives.ActionDirectivesCompiler;
import it.polimi.ingsw.compilers.actions.utils.ActionParameter;
import it.polimi.ingsw.compilers.actions.utils.CompiledAction;
import it.polimi.ingsw.compilers.actions.utils.CompiledActionGroup;
import it.polimi.ingsw.compilers.actions.utils.CompiledExecutableAction;
import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.actions.*;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.utils.IterableRange;
import it.polimi.ingsw.utils.io.XmlUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.mock;

class ActionGroupCompilerTest {

    private List<ActionDirective> directiveList;
    private Die die = mock(Die.class);

    @BeforeEach
    void setUp() throws ClassNotFoundException, SAXException, ParserConfigurationException, IOException {
        this.directiveList = ActionDirectivesCompiler.compile(
                "directives/actions-directives-full.xml",
                true
        );

        Context.getSharedInstance().put("DIE", this.die);
    }

    @Test
    void testEmptyGroup() throws ParserConfigurationException, IOException, SAXException {
        String xmlActionGroup =
                "<action-group>" +
                "</action-group>";

        CompiledActionGroup compiledActionGroup = ActionGroupCompiler.compile(
                XmlUtils.parseXmlString(xmlActionGroup),
                this.directiveList,
                null
        );

        Assertions.assertEquals(ActionGroup.class, compiledActionGroup.getClassToInstantiate());
        Assertions.assertNull(compiledActionGroup.getChooseBetween());
        Assertions.assertEquals(IterableRange.unitaryInteger(), compiledActionGroup.getRepetitions());

        ActionData actionData = compiledActionGroup.getActionData();

        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertNull(actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        Assertions.assertEquals(0, compiledActionGroup.getActions().size());
    }

    @Test
    void testGroupWithFixedRepetitions() throws ParserConfigurationException, IOException, SAXException {
        String xmlActionGroup =
                "<action-group repetitions=\"4\">" +
                "</action-group>";

        CompiledActionGroup compiledActionGroup = ActionGroupCompiler.compile(
                XmlUtils.parseXmlString(xmlActionGroup),
                this.directiveList,
                null
        );

        Assertions.assertEquals(ActionGroup.class, compiledActionGroup.getClassToInstantiate());
        Assertions.assertNull(compiledActionGroup.getChooseBetween());
        Assertions.assertEquals(4, compiledActionGroup.getRepetitions().getStart().intValue());
        Assertions.assertEquals(4, compiledActionGroup.getRepetitions().getEnd().intValue());

        ActionData actionData = compiledActionGroup.getActionData();

        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertNull(actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        Assertions.assertEquals(0, compiledActionGroup.getActions().size());
    }

    @Test
    void testGroupWithRangedRepetitions() throws ParserConfigurationException, IOException, SAXException {
        String xmlActionGroup =
                "<action-group repetitions=\"2..6\">" +
                "</action-group>";

        CompiledActionGroup compiledActionGroup = ActionGroupCompiler.compile(
                XmlUtils.parseXmlString(xmlActionGroup),
                this.directiveList,
                null
        );

        Assertions.assertEquals(ActionGroup.class, compiledActionGroup.getClassToInstantiate());
        Assertions.assertNull(compiledActionGroup.getChooseBetween());
        Assertions.assertEquals(2, compiledActionGroup.getRepetitions().getStart().intValue());
        Assertions.assertEquals(6, compiledActionGroup.getRepetitions().getEnd().intValue());

        ActionData actionData = compiledActionGroup.getActionData();

        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertNull(actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        Assertions.assertEquals(0, compiledActionGroup.getActions().size());
    }

    @Test
    void testGroupWithFixedChooseBetween() throws ParserConfigurationException, IOException, SAXException {
        String xmlActionGroup =
                "<action-group chooseBetween=\"4\">" +
                "</action-group>";

        CompiledActionGroup compiledActionGroup = ActionGroupCompiler.compile(
                XmlUtils.parseXmlString(xmlActionGroup),
                this.directiveList,
                null
        );

        Assertions.assertEquals(ActionGroup.class, compiledActionGroup.getClassToInstantiate());
        Assertions.assertEquals(IterableRange.unitaryInteger(), compiledActionGroup.getRepetitions());
        Assertions.assertEquals(4, compiledActionGroup.getChooseBetween().getStart().intValue());
        Assertions.assertEquals(4, compiledActionGroup.getChooseBetween().getEnd().intValue());

        ActionData actionData = compiledActionGroup.getActionData();

        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertNull(actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        Assertions.assertEquals(0, compiledActionGroup.getActions().size());
    }

    @Test
    void testGroupWithRangedChooseBetween() throws ParserConfigurationException, IOException, SAXException {
        String xmlActionGroup =
                "<action-group chooseBetween=\"2..6\">" +
                "</action-group>";

        CompiledActionGroup compiledActionGroup = ActionGroupCompiler.compile(
                XmlUtils.parseXmlString(xmlActionGroup),
                this.directiveList,
                null
        );

        Assertions.assertEquals(ActionGroup.class, compiledActionGroup.getClassToInstantiate());
        Assertions.assertEquals(IterableRange.unitaryInteger(), compiledActionGroup.getRepetitions());
        Assertions.assertEquals(2, compiledActionGroup.getChooseBetween().getStart().intValue());
        Assertions.assertEquals(6, compiledActionGroup.getChooseBetween().getEnd().intValue());

        ActionData actionData = compiledActionGroup.getActionData();
        
        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertNull(actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        Assertions.assertEquals(0, compiledActionGroup.getActions().size());
    }

    @Test
    void testSimpleGroup() throws ParserConfigurationException, IOException, SAXException {
        String xmlActionGroup =
                "<action-group>" +
                        "   <action effect=\"increment $DIE$ 1\" />" +
                        "   <action effect=\"decrement $DIE$ 1\" />" +
                        "</action-group>";

        CompiledActionGroup compiledActionGroup = ActionGroupCompiler.compile(
                XmlUtils.parseXmlString(xmlActionGroup),
                this.directiveList,
                null
        );

        Assertions.assertEquals(ActionGroup.class, compiledActionGroup.getClassToInstantiate());
        Assertions.assertNull(compiledActionGroup.getChooseBetween());
        Assertions.assertEquals(IterableRange.unitaryInteger(), compiledActionGroup.getRepetitions());

        ActionData actionData = compiledActionGroup.getActionData();

        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertNull(actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        List<CompiledExecutableAction> innerActions = compiledActionGroup.getActions();

        innerActions.forEach(action -> Assertions.assertEquals(action.getClass(), CompiledAction.class));

        CompiledAction firstAction = (CompiledAction) innerActions.get(0);

        Assertions.assertEquals("increment", firstAction.getActionId());
        Assertions.assertEquals(IncrementAction.class, firstAction.getClassToInstantiate());
        Assertions.assertEquals(false, firstAction.requiresUserInteraction());

        ActionData firstActionData = firstAction.getActionData();
        
        Assertions.assertNull(firstActionData.getDescriptionKey());
        Assertions.assertNull(firstActionData.getResultIdentifier());
        Assertions.assertNull(firstActionData.getConstraint());

        List<ActionParameter> firstActionParameters = firstAction.getParameters();
        Assertions.assertEquals(2, firstActionParameters.size());

        Assertions.assertSame(0, firstActionParameters.get(0).getPosition());
        Assertions.assertEquals(Die.class, firstActionParameters.get(0).getType());
        Assertions.assertSame(
                this.die,
                firstActionParameters.get(0).getParameterValue().get(Context.getSharedInstance())
        );
        Assertions.assertFalse(firstActionParameters.get(0).isOptional());
        Assertions.assertNull(firstActionParameters.get(0).getDefaultValue());
        Assertions.assertNull(firstActionParameters.get(0).getOptionalName());

        Assertions.assertSame(1, firstActionParameters.get(1).getPosition());
        Assertions.assertEquals(Integer.class, firstActionParameters.get(1).getType());
        Assertions.assertEquals(
                1,
                firstActionParameters.get(1).getParameterValue().get(Context.getSharedInstance())
        );
        Assertions.assertFalse(firstActionParameters.get(1).isOptional());
        Assertions.assertNull(firstActionParameters.get(1).getDefaultValue());
        Assertions.assertNull(firstActionParameters.get(1).getOptionalName());

        CompiledAction secondAction = (CompiledAction) innerActions.get(1);

        Assertions.assertEquals("decrement", secondAction.getActionId());
        Assertions.assertEquals(DecrementAction.class, secondAction.getClassToInstantiate());
        Assertions.assertEquals(false, secondAction.requiresUserInteraction());

        ActionData secondActionData = secondAction.getActionData();

        Assertions.assertNull(secondActionData.getDescriptionKey());
        Assertions.assertNull(secondActionData.getResultIdentifier());
        Assertions.assertNull(secondActionData.getConstraint());

        List<ActionParameter> secondActionParameters = firstAction.getParameters();
        Assertions.assertEquals(2, secondActionParameters.size());

        Assertions.assertSame(0, secondActionParameters.get(0).getPosition());
        Assertions.assertEquals(Die.class, secondActionParameters.get(0).getType());
        Assertions.assertEquals(
                this.die,
                secondActionParameters.get(0).getParameterValue().get(Context.getSharedInstance())
        );
        Assertions.assertFalse(secondActionParameters.get(0).isOptional());
        Assertions.assertNull(secondActionParameters.get(0).getDefaultValue());
        Assertions.assertNull(secondActionParameters.get(0).getOptionalName());

        Assertions.assertSame(1, secondActionParameters.get(1).getPosition());
        Assertions.assertEquals(Integer.class, secondActionParameters.get(1).getType());
        Assertions.assertEquals(
                1,
                secondActionParameters.get(1).getParameterValue().get(Context.getSharedInstance())
        );
        Assertions.assertFalse(secondActionParameters.get(1).isOptional());
        Assertions.assertNull(secondActionParameters.get(1).getDefaultValue());
        Assertions.assertNull(secondActionParameters.get(1).getOptionalName());
    }

    @Test
    void testComplexGroup() throws ParserConfigurationException, IOException, SAXException {
        String xmlActionGroup =
                "<action-group>\n" +
                "    <action effect=\"increment $DIE$ 1\"/>\n" +
                "    <action effect=\"decrement $DIE$ 1\"/>\n" +
                "    <action-group chooseBetween=\"2\">\n" +
                "        <action effect=\"choose_position $window$\" result=\"POS\"/>\n" +
                "        <action effect=\"pick_at $window$ $POS$\" result=\"DIE\"/>\n" +
                "        <action-group repetitions=\"1..2\">\n" +
                "            <action effect=\"choose_die $window$ [color=$DIE_COLOR$]\" result=\"DIE\"/>\n" +
                "            <action effect=\"pick_die $window$ $DIE$\" result=\"DIE\"/>\n" +
                "            <action effect=\"choose_position_for_die $window$ $DIE$\" result=\"POS\"/>\n" +
                "            <action effect=\"place $DIE$ $window$ $POS$\"/>\n" +
                "        </action-group>\n" +
                "    </action-group>\n" +
                "    <action-group repetitions=\"0..4\" chooseBetween=\"1..7\">\n" +
                "        <action effect=\"choose_die $draft_pool$\" result=\"DIE\" />\n" +
                "        <action effect=\"flip $DIE$\" />\n" +
                "    </action-group>\n" +
                "    <action effect=\"choose_position_for_die $window$ $DIE$ [ignore_color=true]\" result=\"POS\"/>\n" +
                "    <action effect=\"place $DIE$ $window$ $POS$\"/>\n" +
                "</action-group>";

        CompiledActionGroup compiledActionGroup = ActionGroupCompiler.compile(
                XmlUtils.parseXmlString(xmlActionGroup),
                this.directiveList,
                null
        );

        Assertions.assertEquals(ActionGroup.class, compiledActionGroup.getClassToInstantiate());
        Assertions.assertNull(compiledActionGroup.getChooseBetween());
        Assertions.assertEquals(IterableRange.unitaryInteger(), compiledActionGroup.getRepetitions());

        ActionData actionData = compiledActionGroup.getActionData();

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

        Assertions.assertNull(firstInnerInnerInnerActionGroup.getChooseBetween());
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