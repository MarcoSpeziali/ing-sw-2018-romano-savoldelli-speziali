package it.polimi.ingsw.core.constraints;

import it.polimi.ingsw.core.Context;

import java.util.function.Supplier;

public class Constraint implements EvaluableConstraint {

    private String id;

    /**
     *
     */
    private Supplier<Object> leftOperand;

    /**
     *
     */
    private Operator operator;

    /**
     *
     */
    private Supplier<Object> rightOperand;

    /**
     * @return
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return
     */
    public Supplier<Object> getLeftOperand() {
        return this.leftOperand;
    }

    /**
     * @return
     */
    public Operator getOperator() {
        return this.operator;
    }

    /**
     * @return
     */
    public Supplier<Object> getRightOperand() {
        return this.rightOperand;
    }

    /**
     * @param id
     * @param leftOperand
     * @param operator
     * @param rightOperand
     */
    public Constraint(String id, Supplier<Object> leftOperand, Operator operator, Supplier<Object> rightOperand) {
        this.id = id;
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;

        if (!this.validateOperands()) {
            throw new IllegalArgumentException(
                    String.format("The operands must implement Comparable<?> when compared with %s", operator)
            );
        }
    }

    private boolean validateOperands() {
        switch (this.operator) {
            case GREATER:
            case GREATER_EQUAL:
            case LESS:
            case LESS_EQUAL:
                return this.leftOperand instanceof Comparable<?> &&
                        this.rightOperand instanceof Comparable<?>;
            case EQUALS:
            case NOT_EQUALS:
            default:
                return true;
        }
    }

    @Override
    public boolean evaluate(Context context) {
        Object computedLeftOperand = this.leftOperand.get();
        Object computedRightOperand = this.rightOperand.get();

        if (this.operator == Operator.EQUALS) {
            return computedLeftOperand.equals(computedRightOperand);
        }

        if (this.operator == Operator.NOT_EQUALS) {
            return !computedLeftOperand.equals(computedRightOperand);
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
