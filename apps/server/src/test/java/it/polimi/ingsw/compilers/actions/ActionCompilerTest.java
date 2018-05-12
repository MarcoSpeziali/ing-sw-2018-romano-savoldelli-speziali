package it.polimi.ingsw.compilers.actions;

import it.polimi.ingsw.compilers.actions.directives.ActionDirective;
import it.polimi.ingsw.compilers.actions.directives.ActionDirectivesCompiler;
import it.polimi.ingsw.compilers.actions.utils.ActionParameter;
import it.polimi.ingsw.compilers.constraints.ConstraintCompiler;
import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.actions.ActionData;
import it.polimi.ingsw.core.actions.ChooseDieAction;
import it.polimi.ingsw.core.constraints.EvaluableConstraint;
import it.polimi.ingsw.core.locations.ChooseLocation;
import it.polimi.ingsw.utils.io.XmlUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.mock;

class ActionCompilerTest {

    private List<ActionDirective> directiveList;
    private ChooseLocation chooseLocation;

    @BeforeEach
    void setUp() throws ClassNotFoundException, SAXException, ParserConfigurationException, IOException {
        this.directiveList = ActionDirectivesCompiler.compile();

        this.chooseLocation = mock(ChooseLocation.class);
        Context.getSharedInstance().put("draft_pool", this.chooseLocation);
    }

    @Test
    void testSimpleAction() throws IOException, ParserConfigurationException, SAXException {
        String xmlAction = "<action id=\"start\" effect=\"choose_die $draft_pool$\" result=\"DIE\" next=\"inc_dec\"/>";

        CompiledAction compiledAction = ActionCompiler.compile(XmlUtils.parseXmlString(xmlAction), this.directiveList, null);

        Assertions.assertEquals("choose_die", compiledAction.getActionId());
        Assertions.assertEquals(ChooseDieAction.class, compiledAction.getClassToInstantiate());
        Assertions.assertEquals(true, compiledAction.requiresUserInteraction());

        ActionData actionData = compiledAction.getActionData();

        Assertions.assertEquals("start", actionData.getId());
        Assertions.assertEquals("inc_dec", actionData.getNextActionId());
        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertEquals("DIE", actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        List<ActionParameter> parameters = compiledAction.getParameters();
        Assertions.assertEquals(3, parameters.size());

        Assertions.assertEquals(0, parameters.get(0).getPosition().intValue());
        Assertions.assertEquals(ChooseLocation.class, parameters.get(0).getType());
        Assertions.assertSame(
                this.chooseLocation,
                parameters.get(0).getParameterValue().get(Context.getSharedInstance())
        );
        Assertions.assertFalse(parameters.get(0).isOptional());
        Assertions.assertNull(parameters.get(0).getDefaultValue());
        Assertions.assertNull(parameters.get(0).getOptionalName());

        Assertions.assertEquals(1, parameters.get(1).getPosition().intValue());
        Assertions.assertEquals(GlassColor.class, parameters.get(1).getType());
        Assertions.assertNull(parameters.get(1).getParameterValue());
        Assertions.assertTrue(parameters.get(1).isOptional());
        Assertions.assertNull(parameters.get(1).getDefaultValue());
        Assertions.assertEquals("color", parameters.get(1).getOptionalName());

        Assertions.assertEquals(2, parameters.get(2).getPosition().intValue());
        Assertions.assertEquals(Integer.class, parameters.get(2).getType());
        Assertions.assertNull(parameters.get(2).getParameterValue());
        Assertions.assertTrue(parameters.get(2).isOptional());
        Assertions.assertEquals(0, parameters.get(2).getDefaultValue());
        Assertions.assertEquals("shade", parameters.get(2).getOptionalName());
    }

    @Test
    void testActionWithSingleOptionalParameter() throws IOException, SAXException, ParserConfigurationException {
        String xmlAction = "<action id=\"start\" effect=\"choose_die $draft_pool$ [color=red]\" result=\"DIE\" next=\"inc_dec\"/>";

        CompiledAction compiledAction = ActionCompiler.compile(XmlUtils.parseXmlString(xmlAction), this.directiveList, null);

        Assertions.assertEquals("choose_die", compiledAction.getActionId());
        Assertions.assertEquals(ChooseDieAction.class, compiledAction.getClassToInstantiate());
        Assertions.assertEquals(true, compiledAction.requiresUserInteraction());

        ActionData actionData = compiledAction.getActionData();

        Assertions.assertEquals("start", actionData.getId());
        Assertions.assertEquals("inc_dec", actionData.getNextActionId());
        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertEquals("DIE", actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        List<ActionParameter> parameters = compiledAction.getParameters();
        Assertions.assertEquals(3, parameters.size());

        Assertions.assertEquals(0, parameters.get(0).getPosition().intValue());
        Assertions.assertEquals(ChooseLocation.class, parameters.get(0).getType());
        Assertions.assertSame(
                this.chooseLocation,
                parameters.get(0).getParameterValue().get(Context.getSharedInstance())
        );
        Assertions.assertFalse(parameters.get(0).isOptional());
        Assertions.assertNull(parameters.get(0).getDefaultValue());
        Assertions.assertNull(parameters.get(0).getOptionalName());

        Assertions.assertEquals(1, parameters.get(1).getPosition().intValue());
        Assertions.assertEquals(GlassColor.class, parameters.get(1).getType());
        Assertions.assertEquals(GlassColor.RED, parameters.get(1).getParameterValue().get(Context.getSharedInstance()));
        Assertions.assertTrue(parameters.get(1).isOptional());
        Assertions.assertNull(parameters.get(1).getDefaultValue());
        Assertions.assertEquals("color", parameters.get(1).getOptionalName());

        Assertions.assertEquals(2, parameters.get(2).getPosition().intValue());
        Assertions.assertEquals(Integer.class, parameters.get(2).getType());
        Assertions.assertNull(parameters.get(2).getParameterValue());
        Assertions.assertTrue(parameters.get(2).isOptional());
        Assertions.assertEquals(0, parameters.get(2).getDefaultValue());
        Assertions.assertEquals("shade", parameters.get(2).getOptionalName());
    }

    @Test
    void testActionWithMultipleOptionalParameters() throws IOException, SAXException, ParserConfigurationException {
        String xmlAction = "<action id=\"start\" effect=\"choose_die $draft_pool$ [color=red, shade=5]\" result=\"DIE\" next=\"inc_dec\"/>";

        CompiledAction compiledAction = ActionCompiler.compile(XmlUtils.parseXmlString(xmlAction), this.directiveList, null);

        Assertions.assertEquals("choose_die", compiledAction.getActionId());
        Assertions.assertEquals(ChooseDieAction.class, compiledAction.getClassToInstantiate());
        Assertions.assertEquals(true, compiledAction.requiresUserInteraction());

        ActionData actionData = compiledAction.getActionData();

        Assertions.assertEquals("start", actionData.getId());
        Assertions.assertEquals("inc_dec", actionData.getNextActionId());
        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertEquals("DIE", actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        List<ActionParameter> parameters = compiledAction.getParameters();
        Assertions.assertEquals(3, parameters.size());

        Assertions.assertEquals(0, parameters.get(0).getPosition().intValue());
        Assertions.assertEquals(ChooseLocation.class, parameters.get(0).getType());
        Assertions.assertSame(
                this.chooseLocation,
                parameters.get(0).getParameterValue().get(Context.getSharedInstance())
        );
        Assertions.assertFalse(parameters.get(0).isOptional());
        Assertions.assertNull(parameters.get(0).getDefaultValue());
        Assertions.assertNull(parameters.get(0).getOptionalName());

        Assertions.assertEquals(1, parameters.get(1).getPosition().intValue());
        Assertions.assertEquals(GlassColor.class, parameters.get(1).getType());
        Assertions.assertEquals(GlassColor.RED, parameters.get(1).getParameterValue().get(Context.getSharedInstance()));
        Assertions.assertTrue(parameters.get(1).isOptional());
        Assertions.assertNull(parameters.get(1).getDefaultValue());
        Assertions.assertEquals("color", parameters.get(1).getOptionalName());

        Assertions.assertEquals(2, parameters.get(2).getPosition().intValue());
        Assertions.assertEquals(Integer.class, parameters.get(2).getType());
        Assertions.assertEquals(5, parameters.get(2).getParameterValue().get(Context.getSharedInstance()));
        Assertions.assertTrue(parameters.get(2).isOptional());
        Assertions.assertEquals(0, parameters.get(2).getDefaultValue());
        Assertions.assertEquals("shade", parameters.get(2).getOptionalName());
    }

    @Test
    void testWrongNode() {
        String xmlAction = "<cs id=\"start\"/>";

        Assertions.assertThrows(IllegalArgumentException.class, () -> ActionCompiler.compile(XmlUtils.parseXmlString(xmlAction), this.directiveList, null));
    }

    @Test
    void testUnrecognizedAction() {
        String xmlAction = "<action id=\"start\" effect=\"choose_die_nn $draft_pool$ [color=red, shade=5]\" result=\"DIE\" next=\"inc_dec\"/>";

        Assertions.assertThrows(UnrecognizedActionException.class, () -> ActionCompiler.compile(XmlUtils.parseXmlString(xmlAction), this.directiveList, null));
    }

    @Test
    void testMalformedEffect() {
        String xmlAction = "<action id=\"start\" effect=\"213 choose_die co]\" result=\"DIE\" next=\"inc_dec\"/>";

        Assertions.assertThrows(IllegalArgumentException.class, () -> ActionCompiler.compile(XmlUtils.parseXmlString(xmlAction), this.directiveList, null));
    }

    @Test
    void testWithConstraints() throws ParserConfigurationException, SAXException, IOException {
        List<EvaluableConstraint> constraints = List.of(
                ConstraintCompiler.compile(XmlUtils.parseXmlString("<constraint id=\"c1\">1 == 1</constraint>")),
                ConstraintCompiler.compile(XmlUtils.parseXmlString("<constraint id=\"c2\">1 != 1</constraint>"))
        );

        String xmlAction = "<action id=\"start\" effect=\"choose_die $draft_pool$\" result=\"DIE\" next=\"inc_dec\" constraint=\"c1\"/>";

        CompiledAction compiledAction = ActionCompiler.compile(XmlUtils.parseXmlString(xmlAction), this.directiveList, constraints);
        Assertions.assertNotNull(compiledAction.getActionData().getConstraint());
        Assertions.assertTrue(compiledAction.getActionData().getConstraint().evaluate(Context.getSharedInstance()));
    }
}