package it.polimi.ingsw.core.constraints;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.actions.VariableSupplier;

/**
 * Represents a single constraint composed by the operands and an operator.
 */
public class Constraint implements EvaluableConstraint {

    /**
     * The id of the constraint.
     */
    private String id;

    /**
     * The left operand.
     * A {@code Supplier<Object>} id needed to make sure that the operand is as updated as possible.
     */
    private VariableSupplier<Object> leftOperand;

    /**
     * The evaluation's operator.
     */
    private Operator operator;

    /**
     * The right operand.
     * A {@code Supplier<Object>} id needed to make sure that the operand is as updated as possible.
     */
    private VariableSupplier<Object> rightOperand;

    /**
     * @return The id of the constraint.
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * @return The left operand.
     */
    public VariableSupplier<Object> getLeftOperand() {
        return this.leftOperand;
    }

    /**
     * @return The evaluation's operator.
     */
    public Operator getOperator() {
        return this.operator;
    }

    /**
     * @return The right operand.
     */
    public VariableSupplier<Object> getRightOperand() {
        return this.rightOperand;
    }

    /**
     * @param id The id of the constraint.
     * @param leftOperand The left operand.
     * @param operator The evaluation's operator.
     * @param rightOperand The right operand.
     */
    public Constraint(String id, VariableSupplier<Object> leftOperand, Operator operator, VariableSupplier<Object> rightOperand) {
        this.id = id;
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
    }

    /**
     * @param context The context on which the constraints will be ran.
     * @return {@code True} if the operands implements {@code Comparable<?>}, {@code false} otherwise; when {@link #operator}
     * does not represent an equality comparison (>, >=, <, <=).
     * {@code True} if {@link #operator} represents an equality comparison (==, !=).
     */
    private boolean validateOperands(Context context) {
        switch (this.operator) {
            case GREATER:
            case GREATER_EQUAL:
            case LESS:
            case LESS_EQUAL:
                return this.leftOperand.get(context) instanceof Comparable<?> &&
                        this.rightOperand.get(context) instanceof Comparable<?>;
            case EQUALS:
            case NOT_EQUALS:
            default:
                return true;
        }
    }

    @Override
    public boolean evaluate(Context context) {
        if (!this.validateOperands(context)) {
            throw new ClassCastException(
                    String.format("The operands must implement Comparable<?> when compared with %s", operator)
            );
        }

        Object computedLeftOperand = this.leftOperand.get(context);
        Object computedRightOperand = this.rightOperand.get(context);

        if (computedLeftOperand instanceof Number && computedRightOperand instanceof Number) {
            computedLeftOperand = Double.parseDouble(computedLeftOperand.toString());
            computedRightOperand = Double.parseDouble(computedRightOperand.toString());
        }

        if (this.operator == Operator.EQUALS) {
            return computedLeftOperand == null ?
                    computedRightOperand == null :
                    computedLeftOperand.equals(computedRightOperand);
        }

        if (this.operator == Operator.NOT_EQUALS) {
            return !(computedLeftOperand == null ?
                    computedRightOperand == null :
                    computedLeftOperand.equals(computedRightOperand));
        }

        Comparable lop = (Comparable<?>) computedLeftOperand;
        Comparable rop = (Comparable<?>) computedRightOperand;

        switch (this.operator) {
            case GREATER:
                //noinspection unchecked
                return lop.compareTo(rop) > 0;
            case GREATER_EQUAL:
                //noinspection unchecked
                return lop.compareTo(rop) >= 0;
            case LESS:
                //noinspection unchecked
                return lop.compareTo(rop) < 0;
            case LESS_EQUAL:
                //noinspection unchecked
                return lop.compareTo(rop) <= 0;
            default:
                return false;
        }
    }
}
