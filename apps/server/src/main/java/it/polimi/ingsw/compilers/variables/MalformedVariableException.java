package it.polimi.ingsw.compilers.variables;

public class MalformedVariableException extends RuntimeException {
    public MalformedVariableException(String predicate) {
        super("Malformed variable: \"" + predicate + "\"");
    }

    public MalformedVariableException(String predicate, String call) {
        super("Malformed variable: \"" + predicate + "\" near: \"" + call + "\"");
    }

    public MalformedVariableException(String predicate, Class<?> targetClass, String methodName) {
        super(String.format("Could not access method: %s.%s#%s; From predicate: \"%s\"",
                targetClass.getPackageName(),
                targetClass.getName(),
                methodName, predicate
        ));
    }
}
