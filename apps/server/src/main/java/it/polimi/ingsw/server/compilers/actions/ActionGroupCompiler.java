package it.polimi.ingsw.server.compilers.actions;

import it.polimi.ingsw.server.compilers.actions.directives.ActionDirective;
import it.polimi.ingsw.core.actions.ActionData;
import it.polimi.ingsw.core.constraints.EvaluableConstraint;
import it.polimi.ingsw.utils.IterableRange;
import it.polimi.ingsw.utils.io.XMLUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ActionGroupCompiler {

    private ActionGroupCompiler() {}

    /**
     * Compiles a single action-group into its compiles representation: {@link CompiledActionGroup}.
     * @param node the {@link Node} representing the action-group to compile
     * @param directives the {@link List} containing the directives to compile the actions
     * @param constraints the {@link List} of precompiled constraints
     * @return an instance of {@link CompiledActionGroup}
     */
    public static CompiledActionGroup compile(Node node, List<ActionDirective> directives, List<EvaluableConstraint> constraints) {
        // This method only compiles a single <action-group ../>
        if (!node.getNodeName().equals(ActionGroupNodes.ROOT)) {
            throw new IllegalArgumentException("The provided org.w3c.dom.Node must refer to an action-group, instead of a " + node.getNodeName());
        }

        Map<String, Object> actionGroupInfo = XMLUtils.xmlToMap(node);

        // gets the description key
        String description = (String) actionGroupInfo.get(ActionGroupNodes.ACTION_GROUP_DESCRIPTION);

        // gets the constraint id
        String constraintId = (String) actionGroupInfo.get(ActionGroupNodes.ACTION_GROUP_CONSTRAINT);

        // gets the repetitions literal
        String repetitionsString = (String) actionGroupInfo.get(ActionGroupNodes.ACTION_GROUP_REPETITIONS);

        // gets the choose between literal
        String chooseBetweenString = (String) actionGroupInfo.get(ActionGroupNodes.ACTION_GROUP_CHOOSE_BETWEEN);

        // parses the repetitions
        IterableRange<Integer> repetitions = parseRepetitions(repetitionsString);

        // parses the choose between
        IterableRange<Integer> chooseBetween = parseChooseBetween(chooseBetweenString);

        // gets the compiles sub-actions
        List<CompiledExecutableAction> subActions = compileChildren(node.getChildNodes(), directives, constraints);

        // finds the declared constraint between the provided (already compiled) constraints
        EvaluableConstraint constraint = constraintId == null ?
                null :
                constraints.stream()
                        .filter(evaluableConstraint -> evaluableConstraint.getId().equals(constraintId))
                        .findFirst()
                        // if none found an exception must be thrown
                        .orElseThrow(() -> new ConstraintNotFoundException(constraintId));

        return new CompiledActionGroup(
                new ActionData(
                        description,
                        constraint,
                        null
                ),
                subActions,
                repetitions,
                chooseBetween
        );
    }

    /**
     * Parses the string literal for the repetitions number.
     * @param repetitionsString the string literal for the repetitions number
     * @return an instance of {@link IterableRange}
     */
    private static IterableRange<Integer> parseRepetitions(String repetitionsString) {
        // the default value for the chooseBetween range is a single iteration
        if (repetitionsString == null) {
            return IterableRange.unitaryInteger();
        }

        return IterableRange.fromString(
                repetitionsString,
                "..",
                Integer::parseInt,
                IterableRange.INTEGER_INCREMENT_FUNCTION
        );
    }

    /**
     * Parses the string literal for the choose between number.
     * @param chooseBetweenString the string literal for the choose between number
     * @return an instance of {@link IterableRange} if the literal is not {@code null}, {@code null} otherwise
     */
    private static IterableRange<Integer> parseChooseBetween(String chooseBetweenString) {
        // the default value for the chooseBetween range is null
        if (chooseBetweenString == null) {
            return null;
        }

        return IterableRange.fromString(
                chooseBetweenString,
                "..",
                Integer::parseInt,
                IterableRange.INTEGER_INCREMENT_FUNCTION
        );
    }

    private static List<CompiledExecutableAction> compileChildren(NodeList nodeList, List<ActionDirective> directives, List<EvaluableConstraint> constraints) {
        List<CompiledExecutableAction> subActions = new LinkedList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeName().equals(ActionGroupNodes.ACTION_GROUP_SUB_ACTION)) {
                subActions.add(compileSubAction(node, directives, constraints));
            }
            else if (node.getNodeName().equals(ActionGroupNodes.ACTION_GROUP_SUB_ACTION_GROUP)) {
                subActions.add(compileSubActionGroup(node, directives, constraints));
            }
        }

        return subActions;
    }

    /**
     * Compiles a sub-action of the group.
     * @param subAction the sub action
     * @param directives the action directives
     * @param constraints the constraints
     * @return a {@link CompiledAction}
     */
    private static CompiledAction compileSubAction(Node subAction, List<ActionDirective> directives, List<EvaluableConstraint> constraints) {
        if (subAction == null) {
            return null;
        }

        return ActionCompiler.compile(subAction, directives, constraints);
    }

    /**
     * Compiles the sub-actions-groups of the group.
     * @param subActionGroup the sub action-groups
     * @param directives the action directives
     * @param constraints the constraints
     * @return a {@link CompiledActionGroup}
     */
    private static CompiledActionGroup compileSubActionGroup(Node subActionGroup, List<ActionDirective> directives, List<EvaluableConstraint> constraints) {
        if (subActionGroup == null) {
            return null;
        }

        return ActionGroupCompiler.compile(subActionGroup, directives, constraints);
    }

    /**
     * Holds the constants used while accessing the xml nodes and attributes.
     */
    private final class ActionGroupNodes {
        static final String ROOT = "action-group";
        static final String ACTION_GROUP_CHOOSE_BETWEEN = "@chooseBetween";
        static final String ACTION_GROUP_REPETITIONS = "@repetitions";
        static final String ACTION_GROUP_DESCRIPTION = "@description";
        static final String ACTION_GROUP_CONSTRAINT = "@constraint";
        static final String ACTION_GROUP_SUB_ACTION = "action";
        static final String ACTION_GROUP_SUB_ACTION_GROUP = "action-group";

        private ActionGroupNodes() {}
    }
}
