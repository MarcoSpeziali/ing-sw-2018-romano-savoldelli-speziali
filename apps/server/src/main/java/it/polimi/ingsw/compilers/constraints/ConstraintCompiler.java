package it.polimi.ingsw.compilers.constraints;

import it.polimi.ingsw.compilers.expressions.ExpressionCompiler;
import it.polimi.ingsw.core.actions.VariableSupplier;
import it.polimi.ingsw.core.constraints.Constraint;
import it.polimi.ingsw.core.constraints.Operator;
import it.polimi.ingsw.utils.io.XmlUtils;
import org.w3c.dom.Node;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for compiling a single constraint.
 */
public class ConstraintCompiler {

    private ConstraintCompiler() {}

    private static final String CONSTRAINT_NODE_NAME = "constraint";

    /**
     * Compiles a constraint from a {@link Node}.
     * @param node The node holding the constraint
     * @return The compiled constraint
     */
    public static Constraint compile(Node node) {
        // This method only compiles a single <constraint ../>
        if (!node.getNodeName().equals(CONSTRAINT_NODE_NAME)) {
            throw new IllegalArgumentException("The provided org.w3c.dom.Node must refer to a constraint, instead of a " + node.getNodeName());
        }

        // compiles the constraint from the map
        return compile(XmlUtils.xmlToMap(node));
    }

    /**
     * Compiles a constraint from a {@link Map}.
     * @param constraintInfo the constraint info
     * @return the compiled constraint
     */
    public static Constraint compile(Map<String, Object> constraintInfo) {
        String constraintId = (String) constraintInfo.get("@id");
        String constraintText = (String) constraintInfo.get("#text");
        constraintText = constraintText.trim();

        /*
         * (?<lop>[^=!<> ]+): captures 1 or more character but not =, !, <, >, space
         * \s*: matchesOrBlank any whitespaces
         * (?<operator>(==|!=|<=|<|>|>=)): captures == or != or <= or < or > or >=
         * \s*: matchesOrBlank any whitespaces
         * (?<rop>[^=!<> ]+): captures 1 or more character but not =, !, <, >, space
         */
        Pattern pattern = Pattern.compile("(?<lop>[^=!<> ]+)\\s*(?<operator>(==|!=|<=|<|>|>=))\\s*(?<rop>[^=!<> ]+)");
        Matcher matcher = pattern.matcher(constraintText);

        if (matcher.find()) {
            String leftOperand = matcher.group("lop");
            String operator = matcher.group("operator");
            String rightOperand = matcher.group("rop");

            VariableSupplier<Object> leftOperandSupplier;
            VariableSupplier<Object> rightOperandSupplier;

            //noinspection unchecked
            leftOperandSupplier = ExpressionCompiler.compile(leftOperand);
            //noinspection unchecked
            rightOperandSupplier = ExpressionCompiler.compile(rightOperand);

            return new Constraint(constraintId, leftOperandSupplier, Operator.fromString(operator), rightOperandSupplier);
        }
        else {
            // if something was not found than the constraint is malformed
            throw new MalformedConstraintException(constraintText);
        }
    }
}