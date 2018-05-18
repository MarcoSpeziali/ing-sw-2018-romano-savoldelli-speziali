package it.polimi.ingsw.server.compilers.expressions;

/**
 * Thrown when an expression (e.g. $DIE::getShade()$) is malformed.
 */
public class MalformedExpressionException extends RuntimeException {
    private static final long serialVersionUID = -3961762907377292438L;

	/**
     * @param expression The full expression
     */
    public MalformedExpressionException(String expression) {
        super("Malformed variable: \"" + expression + "\"");
    }

    /**
     * @param expression The full expression
     * @param call The part of the expression that caused the error
     */
    public MalformedExpressionException(String expression, String call) {
        super("Malformed variable: \"" + expression + "\" near: \"" + call + "\"");
    }

    /**
     * @param expression The full expression
     * @param targetClass The target class
     * @param methodName The method that generated the exception
     */
    public MalformedExpressionException(String expression, Class<?> targetClass, String methodName) {
        super(String.format("Could not access method: %s.%s#%s; From predicate: \"%s\"",
                targetClass.getPackageName(),
                targetClass.getName(),
                methodName,
                expression
        ));
    }
}
