package it.polimi.ingsw.compilers.objectives;

import it.polimi.ingsw.compilers.instructions.CompiledInstruction;
import it.polimi.ingsw.compilers.instructions.InstructionCompiler;
import it.polimi.ingsw.compilers.instructions.directives.InstructionDirective;
import it.polimi.ingsw.compilers.instructions.predicates.directives.PredicateDirective;
import it.polimi.ingsw.utils.io.XMLUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ObjectiveCompiler {

    private ObjectiveCompiler() {}

    /**
     * @param node the node containing the effect
     * @param instructionDirectives the instruction directives
     * @param predicateDirectives the predicates directives
     * @return an instance of {@link CompiledObjective}
     */
    public static CompiledObjective compile(Node node, List<InstructionDirective> instructionDirectives, List<PredicateDirective> predicateDirectives) {
        // only objective nodes are allowed here
        if (!node.getNodeName().equals(ObjectiveNodes.OBJECTIVE_NODE_NAME)) {
            throw new IllegalArgumentException("The provided org.w3c.dom.Node must refer to an objective, instead of a " + node.getNodeName());
        }

        // gets the effect info
        Map<String, Object> effectInfo = XMLUtils.xmlToMap(node);

        // gets the description
        String description = (String) effectInfo.get(ObjectiveNodes.OBJECTIVE_DESCRIPTION);

        // gets the points per completion
        String pointsPerCompletionString = (String) effectInfo.get(ObjectiveNodes.OBJECTIVE_POINTS_PER_COMPLETION);

        // returns the compiled effect
        return new CompiledObjective(
                description,
                // the default value is 1
                pointsPerCompletionString == null ? 1 : Integer.parseInt(pointsPerCompletionString),
                // compiles the instructions
                compileInstructions(node.getChildNodes(), instructionDirectives, predicateDirectives)
        );
    }

    /**
     * @param children the {@link NodeList} containing the instructions
     * @param instructionDirectives the directives to compile the instructions
     * @param predicateDirectives the predicates directives
     * @return a {@link List} of {@link CompiledInstruction}
     */
    private static List<CompiledInstruction> compileInstructions(NodeList children, List<InstructionDirective> instructionDirectives, List<PredicateDirective> predicateDirectives) {
        List<CompiledInstruction> instructions = new LinkedList<>();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            if (!child.getNodeName().equals("#text")) {
                instructions.add(InstructionCompiler.compile(child, instructionDirectives, predicateDirectives));
            }
        }

        return instructions;
    }

    private class ObjectiveNodes {
        public static final String OBJECTIVE_NODE_NAME = "objective";
        public static final String OBJECTIVE_DESCRIPTION = "@description";
        public static final String OBJECTIVE_POINTS_PER_COMPLETION = "@pointsPerCompletion";
    }
}
