package it.polimi.ingsw.server.constraints;

public class ConstraintEvaluationException extends RuntimeException {
    private static final long serialVersionUID = -4117274887354599334L;

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
