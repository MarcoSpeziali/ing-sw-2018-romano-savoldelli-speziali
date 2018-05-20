package it.polimi.ingsw.server.initializers;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.server.actions.ActionGroup;
import it.polimi.ingsw.server.compilers.actions.ActionGroupCompiler;
import it.polimi.ingsw.server.compilers.actions.CompiledActionGroup;
import it.polimi.ingsw.server.compilers.actions.directives.ActionDirectivesCompiler;
import it.polimi.ingsw.utils.io.XMLUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

class ActionGroupInitializerTest {

    @Test
    void testInitialization() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Die die = new Die(GlassColor.RED, 1);
        Context.getSharedInstance().put("DIE", die);

        String actionGroupText =
                "<action-group repetitions=\"2\">" +
                        "<action effect=\"increment $DIE$ [by=3]\"/>\n" +
                        "<action effect=\"decrement $DIE$ [by=2]\"/>\n" +
                "</action-group>";

        CompiledActionGroup compiledActionGroup = ActionGroupCompiler.compile(
                XMLUtils.parseXmlString(actionGroupText),
                ActionDirectivesCompiler.compile("actions-directives-full.xml", true),
                List.of()
        );

        ActionGroup actionGroup = ActionGroupInitializer.instantiate(compiledActionGroup, Context.getSharedInstance());

        Assertions.assertNotNull(actionGroup);

        actionGroup.run(Context.getSharedInstance());
        Assertions.assertEquals(3, die.getShade().intValue());
    }
}