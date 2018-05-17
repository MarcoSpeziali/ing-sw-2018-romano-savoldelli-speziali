package it.polimi.ingsw.compilers.effects;

import it.polimi.ingsw.compilers.actions.ActionCompiler;
import it.polimi.ingsw.compilers.actions.ActionGroupCompiler;
import it.polimi.ingsw.compilers.actions.CompiledExecutableAction;
import it.polimi.ingsw.compilers.actions.directives.ActionDirective;
import it.polimi.ingsw.core.constraints.EvaluableConstraint;
import it.polimi.ingsw.utils.io.XMLUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EffectCompiler {

    private EffectCompiler() {}

    /**
     * @param node the node containing the effect
     * @param actionDirectives the action directives
     * @param constraints the constraints
     * @return an instance of {@link CompiledEffect}
     */
    public static CompiledEffect compile(Node node, List<ActionDirective> actionDirectives, List<EvaluableConstraint> constraints) {
        // only effect nodes are allowed here
        if (!node.getNodeName().equals(EffectNodes.EFFECT_NODE_NAME)) {
            throw new IllegalArgumentException("The provided org.w3c.dom.Node must refer to an effect, instead of a " + node.getNodeName());
        }

        // gets the effect info
        Map<String, Object> effectInfo = XMLUtils.xmlToMap(node);

        // gets the description
        String description = (String) effectInfo.get(EffectNodes.EFFECT_DESCRIPTION);

        // gets the initial cost
        String initialCostString = (String) effectInfo.get(EffectNodes.EFFECT_INITIAL_COST);

        // returns the compiled effect
        return new CompiledEffect(
                description,
                // the default value is 1
                initialCostString == null ? 1 : Integer.parseInt(initialCostString),
                // compiles the actions
                compileActions(node.getChildNodes(), actionDirectives, constraints)
        );
    }

    /**
     * @param children the {@link NodeList} containing the actions
     * @param actionDirectives the directives to compile the actions
     * @param constraints the constraint to compile the actions
     * @return a {@link List} of {@link CompiledExecutableAction}
     */
    private static List<CompiledExecutableAction> compileActions(NodeList children, List<ActionDirective> actionDirectives, List<EvaluableConstraint> constraints) {
        List<CompiledExecutableAction> actions = new LinkedList<>();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            // action
            if (child.getNodeName().equals(EffectNodes.EFFECT_ACTION_NODE_NAME)) {
                actions.add(ActionCompiler.compile(child, actionDirectives, constraints));
            }
            // action-group
            else if (child.getNodeName().equals(EffectNodes.EFFECT_ACTION_GROUP_NODE_NAME)) {
                actions.add(ActionGroupCompiler.compile(child, actionDirectives, constraints));
            }
        }

        return actions;
    }

    private class EffectNodes {
        public static final String EFFECT_NODE_NAME = "effect";
        public static final String EFFECT_DESCRIPTION = "@description";
        public static final String EFFECT_INITIAL_COST = "@initialCost";
        public static final String EFFECT_ACTION_NODE_NAME = "action";
        public static final String EFFECT_ACTION_GROUP_NODE_NAME = "action-group";
    }
}
