package it.polimi.ingsw.server.compilers.expressions;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.server.instructions.DieFilter;

import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for casting a generic string value to it's corresponding value.
 */
public class ConstantExpressionCaster {

    private static final String INTEGER_PATTERN = "[+\\-]?[0-9]+";
    private static final String BINARY_INTEGER_LITERAL_PATTERN = "[+\\-]?0b[01]{1,32}";
    private static final String HEX_INTEGER_LITERAL_PATTERN = "[+\\-]?0x[0-9A-Fa-f]{1,8}";
    private static final String DOUBLE_PATTERN = "[+\\-]?[0-9]*[.][0-9]+";
    private static final String STRING_PATTERN = "\".*\"";
    private static final String NULL_PATTERN = "null";
    private static final String COLOR_PATTERN = "(red|yellow|blue|green|purple)";
    private static final String DIE_FILTER_PATTERN = "(color|shade)";
    private static final String BOOLEAN_PATTER = "(true|false)";
    // TODO: Add support for arays: [<obj_1>, <obj_2>, ..., <obj_n>]
    // TODO: Add support for lists: (<obj_1>, <obj_2>, ..., <obj_n>)
    // TODO: Add support for maps:  {<key_1>: <obj_1>, <key_2>: <obj_2>, ..., <key_2>: <obj_2>}
    // TODO: Add support for ranges: range(<obj_1>, <obj_2>)

    /**
     * Lazily returns the pattern.
     */
    private static Supplier<Pattern> integerPattern = () -> {
        Pattern pattern = Pattern.compile(INTEGER_PATTERN);
        integerPattern = () -> pattern;
        return pattern;
    };
    
    /**
     * Lazily returns the pattern.
     */
    private static Supplier<Pattern> binaryIntegerLiteralPatter = () -> {
        Pattern pattern = Pattern.compile(BINARY_INTEGER_LITERAL_PATTERN);
        binaryIntegerLiteralPatter = () -> pattern;
        return pattern;
    };
    
    /**
     * Lazily returns the pattern.
     */
    private static Supplier<Pattern> hexIntegerLiteralPatter = () -> {
        Pattern pattern = Pattern.compile(HEX_INTEGER_LITERAL_PATTERN);
        hexIntegerLiteralPatter = () -> pattern;
        return pattern;
    };

    /**
     * Lazily returns the pattern.
     */
    private static Supplier<Pattern> doublePattern = () -> {
        Pattern pattern = Pattern.compile(DOUBLE_PATTERN);
        doublePattern = () -> pattern;
        return pattern;
    };

    /**
     * Lazily returns the pattern.
     */
    private static Supplier<Pattern> stringPattern = () -> {
        Pattern pattern = Pattern.compile(STRING_PATTERN);
        stringPattern = () -> pattern;
        return pattern;
    };

    /**
     * Lazily returns the pattern.
     */
    private static Supplier<Pattern> nullPattern = () -> {
        Pattern pattern = Pattern.compile(NULL_PATTERN, Pattern.CASE_INSENSITIVE);
        nullPattern = () -> pattern;
        return pattern;
    };

    /**
     * Lazily returns the pattern.
     */
    private static Supplier<Pattern> colorPattern = () -> {
        Pattern pattern = Pattern.compile(COLOR_PATTERN, Pattern.CASE_INSENSITIVE);
        colorPattern = () -> pattern;
        return pattern;
    };

    /**
     * Lazily returns the pattern.
     */
    private static Supplier<Pattern> dieFilterPattern = () -> {
        Pattern pattern = Pattern.compile(DIE_FILTER_PATTERN, Pattern.CASE_INSENSITIVE);
        dieFilterPattern = () -> pattern;
        return pattern;
    };

    /**
     * Lazily returns the pattern.
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
    
        matcher = binaryIntegerLiteralPatter.get().matcher(textObject);
        if (matcher.matches()) {
            return Integer.parseInt(textObject.replace("0b", ""), 2);
        }
    
        matcher = hexIntegerLiteralPatter.get().matcher(textObject);
        if (matcher.matches()) {
            return Integer.parseInt(textObject.replace("0x", ""), 16);
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

        matcher = dieFilterPattern.get().matcher(textObject);
        if (matcher.matches()) {
            return DieFilter.fromString(textObject);
        }

        matcher = booleanPattern.get().matcher(textObject);
        if (matcher.matches()) {
            return Boolean.parseBoolean(textObject);
        }

        throw new IllegalArgumentException();
    }
}
