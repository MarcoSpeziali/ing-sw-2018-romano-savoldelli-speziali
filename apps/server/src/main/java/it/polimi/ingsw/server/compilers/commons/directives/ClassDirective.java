package it.polimi.ingsw.server.compilers.commons.directives;

import java.io.Serializable;
import java.util.List;

public class ClassDirective<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 6055327966097014109L;

    /**
     * The id.
     */
    private String id;

    /**
     * The class to instantiate.
     */
    private Class<? extends T> targetClass;

    /**
     * The directives to instantiate the constructor parameters.
     */
    private List<ParameterDirective> parametersDirectives;

    /**
     * @param id                   the id
     * @param targetClass          the class to instantiate
     * @param parametersDirectives the directives to instantiate the parameters
     */
    public ClassDirective(String id, Class<? extends T> targetClass, List<ParameterDirective> parametersDirectives) {
        this.id = id;
        this.targetClass = targetClass;
        this.parametersDirectives = parametersDirectives;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the class to instantiate
     */
    public Class<? extends T> getTargetClass() {
        return targetClass;
    }

    /**
     * @return the directives to instantiate the parameters
     */
    public List<ParameterDirective> getParametersDirectives() {
        return parametersDirectives;
    }
}
