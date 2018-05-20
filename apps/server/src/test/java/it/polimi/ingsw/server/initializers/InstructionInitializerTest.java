package it.polimi.ingsw.server.initializers;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.instructions.ForDieInstruction;
import it.polimi.ingsw.core.instructions.Instruction;
import it.polimi.ingsw.core.instructions.TakeShadeInstruction;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.server.compilers.instructions.CompiledInstruction;
import it.polimi.ingsw.server.compilers.instructions.InstructionCompiler;
import it.polimi.ingsw.server.compilers.instructions.directives.InstructionDirectiveCompiler;
import it.polimi.ingsw.server.compilers.instructions.predicates.directives.PredicateDirectivesCompiler;
import it.polimi.ingsw.utils.io.XMLUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InstructionInitializerTest {

    @Test
    void testInitialization() throws ClassNotFoundException, SAXException, ParserConfigurationException, IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Window window = mock(Window.class);
        when(window.getDice()).thenAnswer(invocation -> List.of(
                new Die(GlassColor.RED, 1),
                new Die(GlassColor.RED, 5),
                new Die(GlassColor.RED, 4),
                new Die(GlassColor.GREEN, 6),
                new Die(GlassColor.GREEN, 3),
                new Die(GlassColor.GREEN, 1),
                new Die(GlassColor.GREEN, 2),
                new Die(GlassColor.PURPLE, 2),
                new Die(GlassColor.BLUE, 5),
                new Die(GlassColor.YELLOW, 2),
                new Die(GlassColor.RED, 2),
                new Die(GlassColor.YELLOW, 4)
        ));

        Context.getSharedInstance().put(Context.WINDOW, window);

        String instructionString = "<for-die die=\"DIE\" filterColor=\"red\"><take-shade die=\"$DIE$\"/></for-die>";

        CompiledInstruction compiledInstruction = InstructionCompiler.compile(
                XMLUtils.parseXmlString(instructionString),
                InstructionDirectiveCompiler.compile("instructions-directives-full.xml", true),
                PredicateDirectivesCompiler.compile()
        );

        Instruction instruction = InstructionInitializer.instantiate(compiledInstruction);

        Assertions.assertNotNull(instruction);
        Assertions.assertEquals(ForDieInstruction.class, instruction.getClass());
        Assertions.assertNotNull(instruction.getInstructions());
        Assertions.assertFalse(instruction.getInstructions().isEmpty());
        Assertions.assertEquals(1, instruction.getInstructions().size());
        Assertions.assertEquals(TakeShadeInstruction.class, instruction.getInstructions().get(0).getClass());
        Assertions.assertEquals(12, instruction.run(Context.getSharedInstance()).intValue());
    }
}