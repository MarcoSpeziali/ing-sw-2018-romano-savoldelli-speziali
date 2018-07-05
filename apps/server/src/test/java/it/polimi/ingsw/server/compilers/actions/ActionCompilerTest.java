package it.polimi.ingsw.server.compilers.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.ChooseLocation;
import it.polimi.ingsw.server.actions.ActionData;
import it.polimi.ingsw.server.actions.ChooseDieAction;
import it.polimi.ingsw.server.compilers.actions.directives.ActionDirective;
import it.polimi.ingsw.server.compilers.actions.directives.ActionDirectivesCompiler;
import it.polimi.ingsw.server.compilers.commons.CompiledParameter;
import it.polimi.ingsw.server.compilers.constraints.ConstraintCompiler;
import it.polimi.ingsw.server.constraints.EvaluableConstraint;
import it.polimi.ingsw.server.utils.VariableSupplier;
import it.polimi.ingsw.utils.io.XMLUtils;
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
        String xmlAction = "<action effect=\"choose_die $draft_pool$\" result=\"DIE\"/>";

        CompiledAction compiledAction = ActionCompiler.compile(XMLUtils.parseXmlString(xmlAction), this.directiveList, null);

        Assertions.assertEquals("choose_die", compiledAction.getActionId());
        Assertions.assertEquals(ChooseDieAction.class, compiledAction.getClassToInstantiate());

        ActionData actionData = compiledAction.getActionData();

        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertEquals("DIE", actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        List<CompiledParameter> parameters = compiledAction.getParameters();
        Assertions.assertEquals(3, parameters.size());

        Assertions.assertEquals(0, parameters.get(0).getPosition().intValue());
        Assertions.assertEquals(VariableSupplier.class, parameters.get(0).getType());
        Assertions.assertSame(
                this.chooseLocation,
                parameters.get(0).getParameterValue().get(Context.getSharedInstance())
        );
        Assertions.assertFalse(parameters.get(0).isOptional());
        Assertions.assertNull(parameters.get(0).getDefaultValue());
        Assertions.assertNull(parameters.get(0).getOptionalName());

        Assertions.assertEquals(1, parameters.get(1).getPosition().intValue());
        Assertions.assertEquals(VariableSupplier.class, parameters.get(1).getType());
        Assertions.assertNull(parameters.get(1).getParameterValue().get(null));
        Assertions.assertTrue(parameters.get(1).isOptional());
        Assertions.assertNull(parameters.get(1).getDefaultValue());
        Assertions.assertEquals("color", parameters.get(1).getOptionalName());

        Assertions.assertEquals(2, parameters.get(2).getPosition().intValue());
        Assertions.assertEquals(VariableSupplier.class, parameters.get(2).getType());
        Assertions.assertEquals(0, parameters.get(2).getParameterValue().get(null));
        Assertions.assertTrue(parameters.get(2).isOptional());
        Assertions.assertEquals(0, parameters.get(2).getDefaultValue());
        Assertions.assertEquals("shade", parameters.get(2).getOptionalName());
    }

    @Test
    void testActionWithSingleOptionalParameter() throws IOException, SAXException, ParserConfigurationException {
        String xmlAction = "<action effect=\"choose_die $draft_pool$ [color=red]\" result=\"DIE\"/>";

        CompiledAction compiledAction = ActionCompiler.compile(XMLUtils.parseXmlString(xmlAction), this.directiveList, null);

        Assertions.assertEquals("choose_die", compiledAction.getActionId());
        Assertions.assertEquals(ChooseDieAction.class, compiledAction.getClassToInstantiate());

        ActionData actionData = compiledAction.getActionData();

        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertEquals("DIE", actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        List<CompiledParameter> parameters = compiledAction.getParameters();
        Assertions.assertEquals(3, parameters.size());

        Assertions.assertEquals(0, parameters.get(0).getPosition().intValue());
        Assertions.assertEquals(VariableSupplier.class, parameters.get(0).getType());
        Assertions.assertSame(
                this.chooseLocation,
                parameters.get(0).getParameterValue().get(Context.getSharedInstance())
        );
        Assertions.assertFalse(parameters.get(0).isOptional());
        Assertions.assertNull(parameters.get(0).getDefaultValue());
        Assertions.assertNull(parameters.get(0).getOptionalName());

        Assertions.assertEquals(1, parameters.get(1).getPosition().intValue());
        Assertions.assertEquals(VariableSupplier.class, parameters.get(1).getType());
        Assertions.assertEquals(GlassColor.RED, parameters.get(1).getParameterValue().get(Context.getSharedInstance()));
        Assertions.assertTrue(parameters.get(1).isOptional());
        Assertions.assertNull(parameters.get(1).getDefaultValue());
        Assertions.assertEquals("color", parameters.get(1).getOptionalName());

        Assertions.assertEquals(2, parameters.get(2).getPosition().intValue());
        Assertions.assertEquals(VariableSupplier.class, parameters.get(2).getType());
        Assertions.assertEquals(0, parameters.get(2).getParameterValue().get(null));
        Assertions.assertTrue(parameters.get(2).isOptional());
        Assertions.assertEquals(0, parameters.get(2).getDefaultValue());
        Assertions.assertEquals("shade", parameters.get(2).getOptionalName());
    }

    @Test
    void testActionWithMultipleOptionalParameters() throws IOException, SAXException, ParserConfigurationException {
        String xmlAction = "<action effect=\"choose_die $draft_pool$ [color=red, shade=5]\" result=\"DIE\"/>";

        CompiledAction compiledAction = ActionCompiler.compile(XMLUtils.parseXmlString(xmlAction), this.directiveList, null);

        Assertions.assertEquals("choose_die", compiledAction.getActionId());
        Assertions.assertEquals(ChooseDieAction.class, compiledAction.getClassToInstantiate());

        ActionData actionData = compiledAction.getActionData();

        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertEquals("DIE", actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        List<CompiledParameter> parameters = compiledAction.getParameters();
        Assertions.assertEquals(3, parameters.size());

        Assertions.assertEquals(0, parameters.get(0).getPosition().intValue());
        Assertions.assertEquals(VariableSupplier.class, parameters.get(0).getType());
        Assertions.assertSame(
                this.chooseLocation,
                parameters.get(0).getParameterValue().get(Context.getSharedInstance())
        );
        Assertions.assertFalse(parameters.get(0).isOptional());
        Assertions.assertNull(parameters.get(0).getDefaultValue());
        Assertions.assertNull(parameters.get(0).getOptionalName());

        Assertions.assertEquals(1, parameters.get(1).getPosition().intValue());
        Assertions.assertEquals(VariableSupplier.class, parameters.get(1).getType());
        Assertions.assertEquals(GlassColor.RED, parameters.get(1).getParameterValue().get(Context.getSharedInstance()));
        Assertions.assertTrue(parameters.get(1).isOptional());
        Assertions.assertNull(parameters.get(1).getDefaultValue());
        Assertions.assertEquals("color", parameters.get(1).getOptionalName());

        Assertions.assertEquals(2, parameters.get(2).getPosition().intValue());
        Assertions.assertEquals(VariableSupplier.class, parameters.get(2).getType());
        Assertions.assertEquals(5, parameters.get(2).getParameterValue().get(Context.getSharedInstance()));
        Assertions.assertTrue(parameters.get(2).isOptional());
        Assertions.assertEquals(0, parameters.get(2).getDefaultValue());
        Assertions.assertEquals("shade", parameters.get(2).getOptionalName());
    }

    @Test
    void testWrongNode() {
        String xmlAction = "<cs id=\"start\"/>";

        Assertions.assertThrows(IllegalArgumentException.class, () -> ActionCompiler.compile(XMLUtils.parseXmlString(xmlAction), this.directiveList, null));
    }

    @Test
    void testUnrecognizedAction() {
        String xmlAction = "<action effect=\"choose_die_nn $draft_pool$ [color=red, shade=5]\" result=\"DIE\"/>";

        Assertions.assertThrows(UnrecognizedActionException.class, () -> ActionCompiler.compile(XMLUtils.parseXmlString(xmlAction), this.directiveList, null));
    }

    @Test
    void testMalformedEffect() {
        String xmlAction = "<action effect=\"213 choose_die co]\" result=\"DIE\"/>";

        Assertions.assertThrows(IllegalArgumentException.class, () -> ActionCompiler.compile(XMLUtils.parseXmlString(xmlAction), this.directiveList, null));
    }

    @Test
    void testWithConstraints() throws ParserConfigurationException, SAXException, IOException {
        List<EvaluableConstraint> constraints = List.of(
                ConstraintCompiler.compile(XMLUtils.parseXmlString("<constraint id=\"c1\">1 == 1</constraint>")),
                ConstraintCompiler.compile(XMLUtils.parseXmlString("<constraint id=\"c2\">1 != 1</constraint>"))
        );

        String xmlAction = "<action effect=\"choose_die $draft_pool$\" result=\"DIE\" constraint=\"c1\"/>";

        CompiledAction compiledAction = ActionCompiler.compile(XMLUtils.parseXmlString(xmlAction), this.directiveList, constraints);
        Assertions.assertNotNull(compiledAction.getActionData().getConstraint());
        Assertions.assertTrue(compiledAction.getActionData().getConstraint().evaluate(Context.getSharedInstance()));
    }

    @Test
    void testExceptionWithUnknownConstraint() {
        List<EvaluableConstraint> constraints = List.of();

        String xmlAction = "<action effect=\"choose_die $draft_pool$\" result=\"DIE\" constraint=\"c1\"/>";

        Assertions.assertThrows(ConstraintNotFoundException.class, () -> ActionCompiler.compile(XMLUtils.parseXmlString(xmlAction), this.directiveList, constraints));

    }
}