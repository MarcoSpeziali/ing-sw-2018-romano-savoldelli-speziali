package it.polimi.ingsw.server.initializers;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.server.actions.Action;
import it.polimi.ingsw.server.actions.IncrementAction;
import it.polimi.ingsw.server.compilers.actions.ActionCompiler;
import it.polimi.ingsw.server.compilers.actions.CompiledAction;
import it.polimi.ingsw.server.compilers.actions.directives.ActionDirectivesCompiler;
import it.polimi.ingsw.utils.io.XMLUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

class ActionInitializerTest {

    @Test
    void testInitialization() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Die die = new Die(GlassColor.RED, 4);
        Context.getSharedInstance().put("DIE", die);

        String actionText = "<action effect=\"increment $DIE$ [by=2]\"/>";

        CompiledAction compiledAction = ActionCompiler.compile(
                XMLUtils.parseXmlString(actionText),
                ActionDirectivesCompiler.compile("actions-directives-full.xml", true),
                List.of()
        );

        Action action = ActionInitializer.instantiate(compiledAction, Context.getSharedInstance());

        Assertions.assertNotNull(action);
        Assertions.assertEquals(IncrementAction.class, action.getClass());

        action.run(Context.getSharedInstance());
        Assertions.assertEquals(6, die.getShade().intValue());
    }
}