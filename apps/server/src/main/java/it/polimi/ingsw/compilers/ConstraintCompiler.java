package it.polimi.ingsw.compilers;

import it.polimi.ingsw.compilers.utils.VariableSupplierCasting;
import it.polimi.ingsw.core.actions.VariableSupplier;
import it.polimi.ingsw.core.constraints.Constraint;
import it.polimi.ingsw.core.constraints.Operator;
import it.polimi.ingsw.utils.io.XmlUtils;
import org.w3c.dom.Node;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: docs
// TODO: Handle function calls (e.g. $DIE::getShade()$)
public class ConstraintCompiler {

    private static final String CONSTRAINT_NODE_NAME = "constraint";

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

        Pattern pattern = Pattern.compile("(?<lop>[^=!<> ]+)\\s*(?<operator>(==|!=|<=|<|>|>=))\\s*(?<rop>[^=!<> ]+)");
        Matcher matcher = pattern.matcher(constraintText);

        try {
            if (matcher.find()) {
                String leftOperand = matcher.group("lop");
                String operator = matcher.group("operator");
                String rightOperand = matcher.group("rop");

                VariableSupplier<Object> leftOperandSupplier;
                VariableSupplier<Object> rightOperandSupplier;

                if (leftOperand.startsWith("$") && leftOperand.endsWith("$")) {
                    String contextKey = leftOperand
                            .replace("$", "")
                            .trim();

                    leftOperandSupplier = context -> context.get(contextKey);
                }
                else {
                    leftOperandSupplier = context -> VariableSupplierCasting.cast(leftOperand);
                }

                if (rightOperand.startsWith("$") && rightOperand.endsWith("$")) {
                    String contextKey = rightOperand
                            .replace("$", "")
                            .trim();

                    rightOperandSupplier = context -> context.get(contextKey);
                }
                else {
                    rightOperandSupplier = context -> VariableSupplierCasting.cast(rightOperand);
                }

                return new Constraint(constraintId, leftOperandSupplier, Operator.fromString(operator), rightOperandSupplier);
            }
            else {
                // TODO: throw new MalformedConstraintException(constraintText)
                return null;
            }
        }
        catch (IllegalArgumentException e) {
            // TODO: throw new MalformedConstraintException(constraintText)
            return null;
        }
    }
}
