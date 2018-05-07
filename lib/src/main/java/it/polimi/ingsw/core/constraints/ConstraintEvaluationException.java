package it.polimi.ingsw.core.constraints;

public class ConstraintEvaluationException extends RuntimeException {
    public ConstraintEvaluationException() {
        super();
    }

    public ConstraintEvaluationException(String constraintId) {
        super(String.format("The evaluation of the constraint %s results in a false result.", constraintId));
    }

    public ConstraintEvaluationException(Object leftOperand, Operator operator, Object rightOperand) {
        super(String.format("The evaluation of the constraint \"%s %s %s\" results in a false result.", leftOperand, operator, rightOperand));
    }
}
