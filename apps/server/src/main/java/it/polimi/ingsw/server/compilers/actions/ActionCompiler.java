package it.polimi.ingsw.server.compilers.actions;

import it.polimi.ingsw.server.compilers.actions.directives.ActionDirective;
import it.polimi.ingsw.server.compilers.commons.ParametersCompiler;
import it.polimi.ingsw.server.actions.ActionData;
import it.polimi.ingsw.server.constraints.EvaluableConstraint;
import it.polimi.ingsw.utils.io.XMLUtils;
import org.w3c.dom.Node;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionCompiler {

    private static final String EFFECT_CALL_REGEX = "^(?<id>[a-zA-Z_][a-zA-Z0-9_]+)(?<params>[^\\[]+)*(\\[(?<opts>.+)])?$";

    private ActionCompiler() {}

    /**
     * Compiles a single action into its compiles representation: {@link CompiledAction}.
     * @param actionNode The {@link Node} representing the action to compile
     * @param directives The {@link List} containing the directives to compile the actions
     * @param constraints The {@link List} of precompiled constraints
     * @return An instance of {@link CompiledAction}
     */
    public static CompiledAction compile(Node actionNode, List<ActionDirective> directives,  List<EvaluableConstraint> constraints) {
        // this method only compiles a single <action ../>
        if (!actionNode.getNodeName().equals(ActionNodes.ROOT)) {
            throw new IllegalArgumentException("The provided org.w3c.dom.Node must refer to an action, instead of a " + actionNode.getNodeName());
        }

        return compile(XMLUtils.xmlToMap(actionNode), directives, constraints);
    }

    /**
     * Compiles a single action into its compiles representation: {@link CompiledAction}.
     * @param actionInfo The {@link Map} representing the action to compile
     * @param directives The {@link List} containing the directives to compile the actions
     * @param constraints The {@link List} of precompiled constraints
     * @return An instance of {@link CompiledAction}
     */
    public static CompiledAction compile(Map<String, Object> actionInfo, List<ActionDirective> directives, List<EvaluableConstraint> constraints) {
        // the description key
        String description = (String) actionInfo.get(ActionNodes.ACTION_DESCRIPTION);

        // the constraint id
        String constraintId = (String) actionInfo.get(ActionNodes.ACTION_CONSTRAINT);

        // the effect as string
        String rawEffect = (String) actionInfo.get(ActionNodes.ACTION_EFFECT);

        // the identifier of the result
        String result = (String) actionInfo.get(ActionNodes.ACTION_RESULT);

        // gets the id of the effect
        String effectId = getEffectId(rawEffect);

        // find the corresponding directive for the current effect id
        ActionDirective targetDirective = directives.stream()
                .filter(actionDirective -> actionDirective.getId().equals(effectId))
                .findFirst()
                // if no directive was found then an exception must be thrown
                .orElseThrow(() -> new UnrecognizedActionException(effectId));

        // finds the declared constraint between the provided (already compiled) constraints
        EvaluableConstraint constraint = constraintId == null ?
                null :
                constraints.stream()
                        .filter(evaluableConstraint -> evaluableConstraint.getId().equals(constraintId))
                        .findFirst()
                        // if none found an exception must be thrown
                        .orElseThrow(() -> new ConstraintNotFoundException(constraintId));

        // finally creating the compiled action
        return new CompiledAction(
                effectId,
                targetDirective.getTargetClass(),
                new ActionData(
                        description,
                        constraint,
                        result
                ),
                ParametersCompiler.parseParameters(rawEffect, targetDirective, EFFECT_CALL_REGEX)
        );
    }

    /**
     * @param rawEffect the effect as string
     * @return the id of the effect
     */
    private static String getEffectId(String rawEffect) {
        Pattern pattern = Pattern.compile(EFFECT_CALL_REGEX);
        Matcher matcher = pattern.matcher(rawEffect);

        if (matcher.find()) {
            return matcher.group("id");
        }

        throw new IllegalArgumentException();
    }

    /**
     * Holds the constants used while accessing the xml nodes and attributes.
     */
    private final class ActionNodes {
        static final String ROOT = "action";
        static final String ACTION_EFFECT = "@effect";
        static final String ACTION_DESCRIPTION = "@description";
        static final String ACTION_CONSTRAINT = "@constraint";
        static final String ACTION_RESULT = "@result";

        private ActionNodes() {}
    }
}
