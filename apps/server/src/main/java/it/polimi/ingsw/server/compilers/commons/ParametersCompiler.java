package it.polimi.ingsw.server.compilers.commons;

import it.polimi.ingsw.server.compilers.commons.directives.ClassDirective;
import it.polimi.ingsw.server.compilers.commons.directives.ParameterDirective;
import it.polimi.ingsw.server.compilers.expressions.ExpressionCompiler;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParametersCompiler {

    private ParametersCompiler() {}

    /**
     * Parses the parameters of a function call.
     * @param rawFunctionCall the function call
     * @param directive the directive
     * @param functionCallRegex the regex that extracts the parameters
     * @return a {@link List} of {@link CompiledParameter}
     */
    public static <T extends Serializable> List<CompiledParameter> parseParameters(String rawFunctionCall, ClassDirective<T> directive, String functionCallRegex) {
        Pattern pattern = Pattern.compile(functionCallRegex);
        Matcher matcher = pattern.matcher(rawFunctionCall);

        // if the raw function call does not match the regex
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
        List<CompiledParameter> actionParameters = parseMandatoryParameters(parameters, directive);
        actionParameters.addAll(parseOptionalParameters(optionalParameters, directive));

        return actionParameters;
    }

    /**
     * Parses the mandatory parameters.
     * @param directive the directive
     * @param rawParameters the array of raw mandatory parameters
     * @return a {@link List} of {@link CompiledParameter}
     */
    private static <T extends Serializable> List<CompiledParameter> parseMandatoryParameters(String[] rawParameters, ClassDirective<T> directive) {
        List<CompiledParameter> parameters = new LinkedList<>();

        for (int i = 0; i < rawParameters.length; i++) {
            int finalI = i;

            // gets the current parameter and throws an exception if it was not found
            ParameterDirective currentParameterDirective = directive.getParametersDirectives().stream()
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
     * @param directive the directive
     * @param rawParameters the array of raw optional parameters
     * @return a {@link List} of {@link CompiledParameter}
     */
    private static <T extends Serializable> List<CompiledParameter> parseOptionalParameters(String[] rawParameters, ClassDirective<T> directive) {
        List<CompiledParameter> actionParameters = new LinkedList<>();

        // filters the directives to get only the optional ones and sorts them by position
        List<ParameterDirective> optionalDirectives = directive.getParametersDirectives().stream()
                .filter(ParameterDirective::isOptional)
                .sorted(Comparator.comparing(ParameterDirective::getPosition))
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
        for (ParameterDirective currentParameterDirective : optionalDirectives) {
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
     * @return an instance of {@link CompiledParameter} from the raw value
     */
    private static CompiledParameter parseParameter(ParameterDirective parameterDirective, String rawParameter, int position) {
        //noinspection unchecked
        return new CompiledParameter(
                parameterDirective.getParameterType(),
                position,
                // the raw value gets compiled since it could be an expression
                rawParameter == null ? null : ExpressionCompiler.compile(rawParameter),
                parameterDirective.getName(),
                parameterDirective.getDefaultValue()
        );
    }
}
