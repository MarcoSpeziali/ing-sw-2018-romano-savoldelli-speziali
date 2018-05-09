package it.polimi.ingsw.compilers.constraints;

import it.polimi.ingsw.compilers.variables.VariableSupplierCompiler;
import it.polimi.ingsw.core.actions.VariableSupplier;
import it.polimi.ingsw.core.constraints.Constraint;
import it.polimi.ingsw.core.constraints.Operator;
import it.polimi.ingsw.utils.io.XmlUtils;
import org.w3c.dom.Node;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        // Gets a map representation of the provided constraint
        Map<String, Object> constraintInfo = XmlUtils.xmlToMap(node);

        String constraintId = (String) constraintInfo.get("@id");
        String constraintText = (String) constraintInfo.get("#text");
        constraintText = constraintText.trim();

        /*
         * (?<lop>[^=!<> ]+): captures 1 or more character but not =, !, <, >, space
         * \s*: matches any whitespaces
         * (?<operator>(==|!=|<=|<|>|>=)): captures == or != or <= or < or > or >=
         * \s*: matches any whitespaces
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

            leftOperandSupplier = VariableSupplierCompiler.compile(leftOperand);
            rightOperandSupplier = VariableSupplierCompiler.compile(rightOperand);

            return new Constraint(constraintId, leftOperandSupplier, Operator.fromString(operator), rightOperandSupplier);
        }
        else {
            // if something was not found than the constraint is malformed
            throw new MalformedConstraintException(constraintText);
        }
    }
}