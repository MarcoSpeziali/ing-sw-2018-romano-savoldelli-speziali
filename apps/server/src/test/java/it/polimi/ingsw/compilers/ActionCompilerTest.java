package it.polimi.ingsw.compilers;

import it.polimi.ingsw.compilers.utils.ActionParameter;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.actions.ActionData;
import it.polimi.ingsw.core.actions.ChooseDieAction;
import it.polimi.ingsw.core.locations.ChooseLocation;
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

class ActionCompilerTest {

    @Test
    void testSimpleAction() throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException {
        ActionCompiler compiler = new ActionCompiler();

        String xmlAction = "<action id=\"start\" effect=\"choose_die $draft_pool$\" result=\"DIE\" next=\"inc_dec\"/>";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlAction));
        Document doc = builder.parse(is);

        CompiledAction compiledAction = compiler.compile(doc.getDocumentElement(), null);

        Assertions.assertEquals("choose_die", compiledAction.getActionId());
        Assertions.assertEquals(ChooseDieAction.class, compiledAction.getClassToInstantiate());
        Assertions.assertEquals(true, compiledAction.getRequiresUserInteraction());

        ActionData actionData = compiledAction.getActionData();

        Assertions.assertEquals("start", actionData.getId());
        Assertions.assertEquals("inc_dec", actionData.getNextActionId());
        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertEquals("DIE", actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        ActionParameter[] parameters = compiledAction.getParameters();
        Assertions.assertEquals(3, parameters.length);

        Assertions.assertSame(0, parameters[0].getPosition());
        Assertions.assertEquals(ChooseLocation.class, parameters[0].getType());
        Assertions.assertEquals("draft_pool", parameters[0].getValue().getRawValue());
        Assertions.assertTrue(parameters[0].getValue().needsToBeComputed());

        Assertions.assertSame(1, parameters[1].getPosition());
        Assertions.assertEquals(GlassColor.class, parameters[1].getType());
        Assertions.assertEquals("null", parameters[1].getValue().getRawValue());
        Assertions.assertFalse(parameters[1].getValue().needsToBeComputed());

        Assertions.assertSame(2, parameters[2].getPosition());
        Assertions.assertEquals(Integer.class, parameters[2].getType());
        Assertions.assertEquals("0", parameters[2].getValue().getRawValue());
        Assertions.assertFalse(parameters[2].getValue().needsToBeComputed());
    }

    @Test
    void testActionWithSingleOptionalParameter() throws ClassNotFoundException, IOException, SAXException, ParserConfigurationException {
        ActionCompiler compiler = new ActionCompiler();

        String xmlAction = "<action id=\"start\" effect=\"choose_die $draft_pool$ [color=red]\" result=\"DIE\" next=\"inc_dec\"/>";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlAction));
        Document doc = builder.parse(is);

        CompiledAction compiledAction = compiler.compile(doc.getDocumentElement(), null);

        Assertions.assertEquals("choose_die", compiledAction.getActionId());
        Assertions.assertEquals(ChooseDieAction.class, compiledAction.getClassToInstantiate());
        Assertions.assertEquals(true, compiledAction.getRequiresUserInteraction());

        ActionData actionData = compiledAction.getActionData();

        Assertions.assertEquals("start", actionData.getId());
        Assertions.assertEquals("inc_dec", actionData.getNextActionId());
        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertEquals("DIE", actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        ActionParameter[] parameters = compiledAction.getParameters();
        Assertions.assertEquals(3, parameters.length);

        Assertions.assertSame(0, parameters[0].getPosition());
        Assertions.assertEquals(ChooseLocation.class, parameters[0].getType());
        Assertions.assertEquals("draft_pool", parameters[0].getValue().getRawValue());
        Assertions.assertTrue(parameters[0].getValue().needsToBeComputed());

        Assertions.assertSame(1, parameters[1].getPosition());
        Assertions.assertEquals(GlassColor.class, parameters[1].getType());
        Assertions.assertEquals("red", parameters[1].getValue().getRawValue());
        Assertions.assertFalse(parameters[1].getValue().needsToBeComputed());

        Assertions.assertSame(2, parameters[2].getPosition());
        Assertions.assertEquals(Integer.class, parameters[2].getType());
        Assertions.assertEquals("0", parameters[2].getValue().getRawValue());
        Assertions.assertFalse(parameters[2].getValue().needsToBeComputed());
    }

    @Test
    void testActionWithMultipleOptionalParameters() throws ClassNotFoundException, IOException, SAXException, ParserConfigurationException {
        ActionCompiler compiler = new ActionCompiler();

        String xmlAction = "<action id=\"start\" effect=\"choose_die $draft_pool$ [color=red, shade=5]\" result=\"DIE\" next=\"inc_dec\"/>";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlAction));
        Document doc = builder.parse(is);

        CompiledAction compiledAction = compiler.compile(doc.getDocumentElement(), null);

        Assertions.assertEquals("choose_die", compiledAction.getActionId());
        Assertions.assertEquals(ChooseDieAction.class, compiledAction.getClassToInstantiate());
        Assertions.assertEquals(true, compiledAction.getRequiresUserInteraction());

        ActionData actionData = compiledAction.getActionData();

        Assertions.assertEquals("start", actionData.getId());
        Assertions.assertEquals("inc_dec", actionData.getNextActionId());
        Assertions.assertNull(actionData.getDescriptionKey());
        Assertions.assertEquals("DIE", actionData.getResultIdentifier());
        Assertions.assertNull(actionData.getConstraint());

        ActionParameter[] parameters = compiledAction.getParameters();
        Assertions.assertEquals(3, parameters.length);

        Assertions.assertSame(0, parameters[0].getPosition());
        Assertions.assertEquals(ChooseLocation.class, parameters[0].getType());
        Assertions.assertEquals("draft_pool", parameters[0].getValue().getRawValue());
        Assertions.assertTrue(parameters[0].getValue().needsToBeComputed());

        Assertions.assertSame(1, parameters[1].getPosition());
        Assertions.assertEquals(GlassColor.class, parameters[1].getType());
        Assertions.assertEquals("red", parameters[1].getValue().getRawValue());
        Assertions.assertFalse(parameters[1].getValue().needsToBeComputed());

        Assertions.assertSame(2, parameters[2].getPosition());
        Assertions.assertEquals(Integer.class, parameters[2].getType());
        Assertions.assertEquals("5", parameters[2].getValue().getRawValue());
        Assertions.assertFalse(parameters[2].getValue().needsToBeComputed());
    }
}