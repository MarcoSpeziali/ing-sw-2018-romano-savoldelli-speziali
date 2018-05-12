package it.polimi.ingsw.compilers.actions;

import it.polimi.ingsw.compilers.actions.directives.ActionDirective;
import it.polimi.ingsw.compilers.actions.directives.ActionParameterDirective;
import it.polimi.ingsw.compilers.actions.utils.ActionParameter;
import it.polimi.ingsw.compilers.actions.utils.CompiledAction;
import it.polimi.ingsw.compilers.expressions.ExpressionCompiler;
import it.polimi.ingsw.core.actions.ActionData;
import it.polimi.ingsw.core.constraints.EvaluableConstraint;
import it.polimi.ingsw.utils.io.XmlUtils;
import org.w3c.dom.Node;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

        return compile(XmlUtils.xmlToMap(actionNode), directives, constraints);
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
                .orElse(null);

        // if no directive was found then an exception must be thrown
        if (targetDirective == null) {
            throw new UnrecognizedActionException(effectId);
        }

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
                parseParameters(targetDirective, rawEffect),
                targetDirective.requiresUserInteraction()
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
     * Parses the effect parameters.
     * @param directive the action directive
     * @param rawEffect the raw effect as string
     * @return a {@link List} of {@link ActionParameter}
     */
    private static List<ActionParameter> parseParameters(ActionDirective directive, String rawEffect) {
        Pattern pattern = Pattern.compile(EFFECT_CALL_REGEX);
        Matcher matcher = pattern.matcher(rawEffect);

        // if the raw effect does not match the regex
        // (actually impossible because this regex has been tested earlier)
        // an exception is thrown
        if (!matcher.find()) {
            throw new IllegalArgumentException();
        }

        String[] parameters = matcher.group("params").trim().split("\\s+");
        String optionalMatch = matcher.group("opts");

        // each optional parameter gets split by = and trimmed
        String[] optionalParameters = optionalMatch == null ?
                null :
                Arrays.stream(optionalMatch.trim()
                        .split(",")
                ).map(String::trim)
                .toArray(String[]::new);

        // the parameters gets parsed and returned
        List<ActionParameter> actionParameters = parseMandatoryParameters(directive, parameters);
        actionParameters.addAll(parseOptionalParameters(directive, optionalParameters));

        return actionParameters;
    }

    /**
     * Parses the mandatory parameters.
     * @param directive the action directive
     * @param rawParameters the array of raw mandatory parameters
     * @return a {@link List} of {@link ActionParameter}
     */
    private static List<ActionParameter> parseMandatoryParameters(ActionDirective directive, String[] rawParameters) {
        List<ActionParameter> parameters = new LinkedList<>();

        for (int i = 0; i < rawParameters.length; i++) {
            int finalI = i;

            // gets the current parameter and throws an exception if it was not found
            ActionParameterDirective currentParameterDirective = directive.getParametersDirectives()
                    .stream()
                    .filter(actionParameterDirective -> actionParameterDirective.getPosition().equals(finalI))
                    .findFirst()
                    .orElseThrow(() -> new MissingParameterException(directive.getId()));

            // parses the current parameter
            parameters.add(parseParameter(currentParameterDirective, rawParameters[i], i));
        }

        return parameters;
    }

    /**
     * Parses the mandatory parameters.
     * @param directive the action directive
     * @param rawParameters the array of raw optional parameters
     * @return a {@link List} of {@link ActionParameter}
     */
    private static List<ActionParameter> parseOptionalParameters(ActionDirective directive, String[] rawParameters) {
        List<ActionParameter> actionParameters = new LinkedList<>();

        // filters the directives to get only the optional ones and sorts them by position
        List<ActionParameterDirective> optionalDirectives = directive.getParametersDirectives().stream()
                .filter(ActionParameterDirective::isOptional)
                .sorted(Comparator.comparing(ActionParameterDirective::getPosition))
                .collect(Collectors.toList());

        // if no optional were provided then the list gets created immediately with null as value
        if (rawParameters == null) {
            return optionalDirectives.stream()
                    .map(actionParameterDirective -> parseParameter(
                            actionParameterDirective,
                            null,
                            actionParameterDirective.getPosition()
                    ))
                    .collect(Collectors.toList());
        }

        // holds the param_name: param_value pairs
        Map<String, String> parameters = new HashMap<>();

        // every raw parameters gets split by = (limit 2: means that only the first occurrence will cause a split
        Arrays.stream(rawParameters)
                .map(s -> s.split("=", 2))
                // the result is param_name: param_value, which gets added into the map
                .forEach(strings -> parameters.put(strings[0], strings[1]));

        // for each directive a parameters gets parsed
        for (ActionParameterDirective currentParameterDirective : optionalDirectives) {
            actionParameters.add(parseParameter(
                    currentParameterDirective,
                    parameters.get(currentParameterDirective.getName()),
                    currentParameterDirective.getPosition()
            ));
        }

        return actionParameters;
    }

    /**
     * Parses a single parameter.
     * @param parameterDirective the directive of the parameter
     * @param rawParameter the raw value of the parameter
     * @param position the position of the parameter in the constructor
     * @return an instance of {@link ActionParameter} from the raw value
     */
    private static ActionParameter parseParameter(ActionParameterDirective parameterDirective, String rawParameter, int position) {
        //noinspection unchecked
        return new ActionParameter(
                parameterDirective.getParameterType(),
                position,
                // the raw value gets compiled since it could be an expression
                rawParameter == null ? null : ExpressionCompiler.compile(rawParameter),
                parameterDirective.getName(),
                parameterDirective.getDefaultValue()
        );
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
