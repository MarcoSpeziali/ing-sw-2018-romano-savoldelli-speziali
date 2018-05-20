package it.polimi.ingsw.server.compilers.expressions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.server.utils.VariableSupplier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for compiling an expression which can contain:
 * - a global variable (e.g. $DIE$);
 * - a static value (e.g. 12, "string", red, ...);
 * - a chained function call (with parameters) to a global variable (e.g. $DIE::getShade()::compareTo($OTHER_DIE::getShade()$)$
 */
public class ExpressionCompiler {

    /**
     * Declared so this class is not instantiable
     */
    private ExpressionCompiler() {}

    /**
     * Compiles the expression into a {@link VariableSupplier}.
     * @param variableString The expression to compile.
     * @return The compiled expression.
     */
    public static VariableSupplier compile(String variableString) {
        if (variableString == null) {
            return context -> null;
        }

        // if the expression starts and ends with $ ($ symbols in strings must be escaped with \$)
        if (variableString.startsWith("$") && variableString.endsWith("$")) {
            // useless characters (whitespaces and the starting and ending $) gets removed
            String predicate = variableString
                    .substring(1, variableString.length() - 1)
                    .trim();

            // the expression needs to be processed
            return getVariableSupplierFromPredicate(predicate);
        }
        else {
            // the expression does not need to be processed, it is immediately converted to the
            // corresponding object and returned
            return context -> ConstantExpressionCaster.cast(variableString);
        }
    }

    /**
     * Returns the compiled {@link VariableSupplier} from a predicate.
     * @param predicate The predicate to compile
     * @return The compiled {@link VariableSupplier}
     */
    private static VariableSupplier getVariableSupplierFromPredicate(String predicate) {
        // splits the outer function/field call
        String[] calls = splitPredicate(predicate);

        // if there is only a call then the result is a variable in the context
        if (calls.length == 1) {
            return context -> context.get(predicate);
        }
        else {
            // otherwise a more complex process must be done, which is required to be executed runtime
            return context -> {
                // the target object is the first call which gets retrieved in the context
                Object target = context.get(calls[0]);

                // than every other call must be applied to the target which gets replaced by the call result
                for (int i = 1; i < calls.length; i++) {
                    if (isFunctionCall(calls[i])) {
                        // if the call is a function it gets called
                        target = executeFunction(target, calls[i], predicate, context);
                    }
                    else if (isFieldAccess(calls[i])) {
                        // if the call refers to a field its value replaces the target
                        target = getField(target, calls[i], predicate);
                    }
                    else {
                        // otherwise an exception is thrown
                        throw new MalformedExpressionException(predicate, calls[i]);
                    }
                }

                // finally the last result in the chain gets returned
                return target;
            };
        }
    }

    /**
     * Checks if the predicate is a function call
     * @param call The predicate.
     * @return {@code true} is the predicate represents a function call else {@code false}.
     */
    private static boolean isFunctionCall(String call) {
        // the predicate represents a function call if matchesOrBlank the regex
        /*
         * ^: beginning of a line
         * [a-zA-Z_]: a single character from a to z or A to Z or the character _
         * [a-zA-Z0-9_]: a single character from a to z or A to Z or 0 to 9 or the character _
         *      *: matchesOrBlank 0 or an infinite number of times the expression [a-zA-Z0-9_]
         * \(: matchesOrBlank the character (
         * .: matchesOrBlank any character
         *      *: matchesOrBlank 0 or an infinite number of times the expression .
         * \): matchesOrBlank the character )
         * $: ending of a line
         */
        return call.matches("^[a-zA-Z_][a-zA-Z0-9_]*\\(.*\\)$");
    }

    /**
     * Checks if the predicate is a field access
     * @param call The predicate.
     * @return {@code true} is the predicate represents a function call else {@code false}.
     */
    private static boolean isFieldAccess(String call) {
        // the predicate represents a function call if matchesOrBlank the regex
        /*
         * ^: beginning of a line
         * [a-zA-Z_]: a single character from a to z or A to Z or the character _
         * [a-zA-Z0-9_]: a single character from a to z or A to Z or 0 to 9 or the character _
         *      *: matchesOrBlank 0 or an infinite number of times the expression [a-zA-Z0-9_]
         * $: ending of a line
         */
        return call.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }

    /**
     * Executes the provided function (by name) on the provided target.
     * @param target The object on which call the function
     * @param call The function call <name>(<parameters>)
     * @param predicate The full predicate, used in case of an exception
     * @param context The context
     * @return The result of the function call
     */
    private static Object executeFunction(Object target, String call, String predicate, Context context) {
        /*
         * ^: beginning of a line
         * (?<name>[a-zA-Z_][a-zA-Z0-9_]*): captures the name of the function (named group)
         * \(: matchesOrBlank the character (
         * \s*: matchesOrBlank 0 or more whitespaces
         * (?<params>.+)?: captures the parameters of the function (named group, optional)
         * \s*: matchesOrBlank 0 or more whitespaces
         * \): matchesOrBlank the character )
         * $: ending of a line
         */
        Pattern functionCallPattern = Pattern.compile("^(?<name>[a-zA-Z_][a-zA-Z0-9_]*)\\(\\s*(?<params>.+)?\\s*\\)$");
        Matcher matcher = functionCallPattern.matcher(call);

        // if nothing was found then the function call is malformed
        // but this has been tested earlier
        //noinspection ResultOfMethodCallIgnored
        matcher.find();

        String methodName = matcher.group("name");
        String methodParams = matcher.group("params");

        // if parameters exist then their value gets retrieved
        Object[] params = methodParams == null ? null : getParametersFromPredicate(methodParams);

        try {
            // retrieves the method
            Method method = getMethodForPredicateWithParameters(target, methodName, params, context);

            if (params == null) {
                // if no parameters the method gets executed directly
                target = method.invoke(target);
            }
            else {
                // otherwise the parameters gets calculated
                params = Arrays.stream(params)
                        .map(o -> o instanceof VariableSupplier ?
                                ((VariableSupplier) o).get(context) :
                                o)
                        .toArray();

                // and the method invoked
                target = method.invoke(target, params);
            }
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new MalformedExpressionException(predicate, target.getClass(), methodName);
        }

        // finally returns the result
        return target;
    }

    /**
     * Gets the value of the specified field.
     * @param target The target object
     * @param fieldName The field name
     * @param predicate The full predicate, used in case of an exception
     * @return The value of the specified field.
     */
    private static Object getField(Object target, String fieldName, String predicate) {
        try {
            // the target is replaced with the field value
            target = target.getClass().getField(fieldName).get(target);
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            throw new MalformedExpressionException(predicate);
        }

        return target;
    }

    /**
     * Retrieves the method specified.
     * @param obj The object owning the method
     * @param methodName The method name
     * @param paramsValue The parameters accepted by the method
     * @param context The context
     * @return The specified method
     * @throws NoSuchMethodException if the method does not exists
     */
    private static Method getMethodForPredicateWithParameters(Object obj, String methodName, Object[] paramsValue, Context context) throws NoSuchMethodException {
        if (paramsValue != null) {
            // if the parameters are not null then to find the method the parameters classes are needed
            Class<?>[] paramsClasses = Arrays.stream(paramsValue)
                    .map(o -> o instanceof VariableSupplier ?
                            // if there are some VariableSuppliers their value has to be retrieved
                            ((VariableSupplier) o).get(context).getClass() :
                            o.getClass())
                    .toArray(Class[]::new);

            return obj.getClass().getMethod(methodName, paramsClasses);
        } else {
            return obj.getClass().getMethod(methodName);
        }
    }

    /**
     * Retrieves the actual value of the parameters.
     * @param paramsString A comma-separated parameters list
     * @return The values of the parameters
     */
    private static Object[] getParametersFromPredicate(String paramsString) {
        // the list gets split by , and each element gets trimmed
        String[] params = Arrays.stream(paramsString.split(","))
                .map(String::trim)
                .toArray(String[]::new);

        Object[] result = new Object[params.length];

        for (int i = 0; i < result.length; i++) {
            String currentParam = params[i];

            // for each parameters checks if it needs to be compiled or if it is constant
            if (currentParam.startsWith("$") && currentParam.endsWith("$")) {
                result[i] = ExpressionCompiler.compile(currentParam.trim());
            }
            else {
                result[i] = ConstantExpressionCaster.cast(params[i]);
            }
        }

        return result;
    }

    /**
     * Splits the predicate in its functions/fields calls.
     * @param predicate The predicate to split
     * @return An array of calls
     */
    private static String[] splitPredicate(String predicate) {
        /*
         * since creating a regex that does not split the string :: in an expression like this:
         * test_class::useDie($DIE$, $test_class::getA()$, "test") where the expected result would be:
         * {"test_class", "useDie($DIE$, $test_class::getA()$, "test")"}. If the predicate gets split by ::
         * the result would be: {"test_class", "useDie($DIE$, $test_class", "getA()$, "test")"}. Which is incorrect.
         * The best solution I (MarcoSpeziali) found is to replace any occurrences of $..$ with {i}, where 'i' is
         * the index of the substitution. After the substitution the prior expression would be:
         * test_class::useDie({0}, {1}, "test"). Every substitution gets added into #substitutions (LinkedList)
         */
        List<String> substitutions = new LinkedList<>();

        /*
         * (?<=[^\\]): ensures that before the next regex there is not a \
         * \$: matchesOrBlank the character $
         * (.*?): matchesOrBlank (and groups) any character but as few as possible
         * (?<=[^\\]): ensures that before the next regex there is not a \
         * \$: matchesOrBlank the character $
         */
        Pattern pattern = Pattern.compile("(?<=[^\\\\])\\$(.*?)(?<=[^\\\\])\\$");
        Matcher matcher = pattern.matcher(predicate);

        // while there is a match of the regex that the matched string gets substituted
        int foundIndex = 0;
        while (matcher.find()) {
            String found = matcher.group();

            substitutions.add(found);
            predicate = predicate.replace(found, String.format("{%d}", foundIndex));

            foundIndex++;
        }

        // after the substitution the predicate can be split
        String[] calls = predicate.split("::");

        // and after the split the removed expressions gets substituted in the split array
        for (int i = 0; i < substitutions.size(); i++) {
            for (int j = 0; j < calls.length; j++) {
                calls[j] = calls[j].replace(String.format("{%s}", i), substitutions.get(i));
            }
        }

        return calls;
    }
}
