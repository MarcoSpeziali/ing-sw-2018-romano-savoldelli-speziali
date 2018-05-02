package it.polimi.ingsw.compilers.utils;

import it.polimi.ingsw.core.GlassColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: doc
public class VariableSupplierCasting {

    private static final Pattern INTEGER_PATTERN = Pattern.compile("[+-]?[0-9]+");
    private static final Pattern FLOAT_PATTERN = Pattern.compile("[+-]?[0-9]*[.][0-9]+");
    private static final Pattern STRING_PATTERN = Pattern.compile("\".*\"");
    private static final Pattern NULL_PATTERN = Pattern.compile("null", Pattern.CASE_INSENSITIVE);
    private static final Pattern COLOR_PATTER = Pattern.compile("(red|yellow|blue|green|purple)", Pattern.CASE_INSENSITIVE);

    public static Object cast(String textObject) {
        Matcher matcher = INTEGER_PATTERN.matcher(textObject);

        if (matcher.matches()) {
            return Integer.parseInt(textObject);
        }

        matcher = FLOAT_PATTERN.matcher(textObject);

        if (matcher.matches()) {
            return Double.parseDouble(textObject);
        }

        matcher = STRING_PATTERN.matcher(textObject);

        if (matcher.matches()) {
            return textObject.replace("\"", "");
        }

        matcher = NULL_PATTERN.matcher(textObject);

        if (matcher.matches()) {
            return null;
        }

        matcher = COLOR_PATTER.matcher(textObject);

        if (matcher.matches()) {
            return GlassColor.fromString(textObject);
        }

        throw new IllegalArgumentException();
    }
}
