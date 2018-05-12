package it.polimi.ingsw.compilers.actions;

import it.polimi.ingsw.compilers.actions.directives.ActionDirective;
import it.polimi.ingsw.core.actions.ActionData;
import it.polimi.ingsw.core.constraints.EvaluableConstraint;
import it.polimi.ingsw.utils.IterableRange;
import it.polimi.ingsw.utils.io.XmlUtils;
import org.w3c.dom.Node;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        return compile(XmlUtils.xmlToMap(node), directives, constraints);
    }

    /**
     * Compiles a single action-group into its compiles representation: {@link CompiledActionGroup}.
     * @param actionGroupInfo the {@link Map} representing the action to compile
     * @param directives the {@link List} containing the directives to compile the actions
     * @param constraints the {@link List} of precompiled constraints
     * @return an instance of {@link CompiledActionGroup}
     */
    public static CompiledActionGroup compile(Map<String, Object> actionGroupInfo, List<ActionDirective> directives, List<EvaluableConstraint> constraints) {
        // gets the id of the constraint
        String id = (String) actionGroupInfo.get(ActionGroupNodes.ACTION_GROUP_ID);

        // gets the next actions
        String next = (String) actionGroupInfo.get(ActionGroupNodes.ACTION_GROUP_NEXT_ID);

        // gets the description key
        String description = (String) actionGroupInfo.get(ActionGroupNodes.ACTION_GROUP_DESCRIPTION);

        // gets the constraint id
        String constraintId = (String) actionGroupInfo.get(ActionGroupNodes.ACTION_GROUP_CONSTRAINT);

        // gets the root action id for the
        String root = (String) actionGroupInfo.get(ActionGroupNodes.ACTION_GROUP_ROOT);

        // gets the repetitions literal
        String repetitionsString = (String) actionGroupInfo.get(ActionGroupNodes.ACTION_GROUP_REPETITIONS);

        // gets the choose between literal
        String chooseBetweenString = (String) actionGroupInfo.get(ActionGroupNodes.ACTION_GROUP_CHOOSE_BETWEEN);

        // parses the repetitions
        IterableRange<Integer> repetitions = parseRepetitions(repetitionsString);

        // parses the choose between
        IterableRange<Integer> chooseBetween = parseChooseBetween(chooseBetweenString);

        // gets the compiles sub-actions
        List<CompiledExecutableAction> subActions = new LinkedList<>();

        subActions.addAll(
                compileSubActions(XmlUtils.getMapArrayAnyway(
                        actionGroupInfo, ActionGroupNodes.ACTION_GROUP_SUB_ACTION
                ), directives, constraints)
        );

        // gets the compiles sub-action-groups
        subActions.addAll(compileSubActionGroups(XmlUtils.getMapArrayAnyway(
                actionGroupInfo, ActionGroupNodes.ROOT
        ), directives, constraints));

        // finds the declared constraint between the provided (already compiled) constraints
        EvaluableConstraint constraint = constraintId == null ?
                null :
                constraints.stream()
                        .filter(evaluableConstraint -> evaluableConstraint.getId().equals(constraintId))
                        .findFirst()
                        // if none found an exception must be thrown
                        .orElseThrow(() -> new ConstraintNotFoundException(constraintId, id));

        return new CompiledActionGroup(
                new ActionData(
                        id,
                        next,
                        description,
                        constraint,
                        null
                ),
                root,
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

    /**
     * Compiles the sub-actions of the group.
     * @param subActions the sub actions
     * @param directives the action directives
     * @param constraints the constraints
     * @return a {@link List} of {@link CompiledExecutableAction}
     */
    private static List<CompiledExecutableAction> compileSubActions(Map<String, Object>[] subActions, List<ActionDirective> directives, List<EvaluableConstraint> constraints) {
        if (subActions == null) {
            return List.of();
        }

        return Arrays.stream(subActions)
                .map(stringObjectMap -> ActionCompiler.compile(stringObjectMap, directives, constraints))
                .collect(Collectors.toList());
    }

    /**
     * Compiles the sub-actions-groups of the group.
     * @param subActionGroups the sub action-groups
     * @param directives the action directives
     * @param constraints the constraints
     * @return a {@link List} of {@link CompiledExecutableAction}
     */
    private static List<CompiledExecutableAction> compileSubActionGroups(Map<String, Object>[] subActionGroups, List<ActionDirective> directives, List<EvaluableConstraint> constraints) {
        if (subActionGroups == null) {
            return List.of();
        }

        return Arrays.stream(subActionGroups)
                .map(stringObjectMap -> ActionGroupCompiler.compile(stringObjectMap, directives, constraints))
                .collect(Collectors.toList());
    }

    /**
     * Holds the constants used while accessing the xml nodes and attributes.
     */
    private final class ActionGroupNodes {
        static final String ROOT = "action-group";
        static final String ACTION_GROUP_ID = "@id";
        static final String ACTION_GROUP_CHOOSE_BETWEEN = "@chooseBetween";
        static final String ACTION_GROUP_REPETITIONS = "@repetitions";
        static final String ACTION_GROUP_NEXT_ID = "@next";
        static final String ACTION_GROUP_ROOT = "@root";
        static final String ACTION_GROUP_DESCRIPTION = "@description";
        static final String ACTION_GROUP_CONSTRAINT = "@constraint";
        static final String ACTION_GROUP_SUB_ACTION = "action";

        private ActionGroupNodes() {}
    }
}
