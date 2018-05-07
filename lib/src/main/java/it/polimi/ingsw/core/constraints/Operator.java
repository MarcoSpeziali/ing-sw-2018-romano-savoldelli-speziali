package it.polimi.ingsw.core.constraints;

public enum Operator {
    GREATER(">"),
    GREATER_EQUAL(">="),
    LESS("<"),
    LESS_EQUAL("<="),
    EQUALS("=="),
    NOT_EQUALS("!=");

    private final String stringRepresentation;

    Operator(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

    public static Operator fromString(String stringRepresentation) {
        switch (stringRepresentation) {
            case ">":
                return Operator.GREATER;
            case ">=":
                return Operator.GREATER_EQUAL;
            case "<":
                return Operator.LESS;
            case "<=":
                return Operator.LESS_EQUAL;
            case "==":
                return Operator.EQUALS;
            case "!=":
                return Operator.NOT_EQUALS;
            default:
                throw new IllegalArgumentException("Unrecognized operator: " + stringRepresentation);
        }
    }

    @Override
    public String toString() {
        return this.stringRepresentation;
    }
}
