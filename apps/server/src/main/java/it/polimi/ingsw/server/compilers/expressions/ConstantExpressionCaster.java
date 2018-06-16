package it.polimi.ingsw.server.compilers.expressions;

import it.polimi.ingsw.core.GlassColor;

import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for casting a generic string value to it's corresponding value.
 */
public class ConstantExpressionCaster {

    private static final String INTEGER_PATTERN = "[+-]?[0-9]+";
    private static final String DOUBLE_PATTERN = "[+-]?[0-9]*[.][0-9]+";
    private static final String STRING_PATTERN = "\".*\"";
    private static final String NULL_PATTERN = "null";
    private static final String COLOR_PATTERN = "(red|yellow|blue|green|purple)";
    private static final String BOOLEAN_PATTER = "(true|false)";
    // TODO: Add support for arays: [<obj_1>, <obj_2>, ..., <obj_n>]
    // TODO: Add support for lists: (<obj_1>, <obj_2>, ..., <obj_n>)
    // TODO: Add support for maps:  {<key_1>: <obj_1>, <key_2>: <obj_2>, ..., <key_2>: <obj_2>}
    // TODO: Add support for ranges: range(<obj_1>, <obj_2>)

    /**
     * Lazily return the pattern.
     */
    private static Supplier<Pattern> integerPattern = () -> {
        Pattern pattern = Pattern.compile(INTEGER_PATTERN);
        integerPattern = () -> pattern;
        return pattern;
    };

    /**
     * Lazily return the pattern.
     */
    private static Supplier<Pattern> doublePattern = () -> {
        Pattern pattern = Pattern.compile(DOUBLE_PATTERN);
        doublePattern = () -> pattern;
        return pattern;
    };

    /**
     * Lazily return the pattern.
     */
    private static Supplier<Pattern> stringPattern = () -> {
        Pattern pattern = Pattern.compile(STRING_PATTERN);
        stringPattern = () -> pattern;
        return pattern;
    };

    /**
     * Lazily return the pattern.
     */
    private static Supplier<Pattern> nullPattern = () -> {
        Pattern pattern = Pattern.compile(NULL_PATTERN, Pattern.CASE_INSENSITIVE);
        nullPattern = () -> pattern;
        return pattern;
    };

    /**
     * Lazily return the pattern.
     */
    private static Supplier<Pattern> colorPattern = () -> {
        Pattern pattern = Pattern.compile(COLOR_PATTERN, Pattern.CASE_INSENSITIVE);
        colorPattern = () -> pattern;
        return pattern;
    };

    /**
     * Lazily return the pattern.
     */
    private static Supplier<Pattern> booleanPattern = () -> {
        Pattern pattern = Pattern.compile(BOOLEAN_PATTER, Pattern.CASE_INSENSITIVE);
        booleanPattern = () -> pattern;
        return pattern;
    };

    private ConstantExpressionCaster() {
    }

    /**
     * Casts a string object into its corresponding value.
     *
     * @param textObject The object to cast.
     * @return The casted object.
     * @throws IllegalArgumentException if the object cannot be casted
     */
    public static Object cast(String textObject) {
        Matcher matcher = integerPattern.get().matcher(textObject);
        if (matcher.matches()) {
            return Integer.parseInt(textObject);
        }

        matcher = doublePattern.get().matcher(textObject);
        if (matcher.matches()) {
            return Double.parseDouble(textObject);
        }

        matcher = stringPattern.get().matcher(textObject);
        if (matcher.matches()) {
            // since a string must start and end with a \" it needs to get replaced
            // as well as \$ and \\"
            return textObject.replace("\"", "").replace("\\$", "$").replace("\\", "\"");
        }

        matcher = nullPattern.get().matcher(textObject);
        if (matcher.matches()) {
            return null;
        }

        matcher = colorPattern.get().matcher(textObject);
        if (matcher.matches()) {
            return GlassColor.fromString(textObject);
        }

        matcher = booleanPattern.get().matcher(textObject);
        if (matcher.matches()) {
            return Boolean.parseBoolean(textObject);
        }

        throw new IllegalArgumentException();
    }
}
