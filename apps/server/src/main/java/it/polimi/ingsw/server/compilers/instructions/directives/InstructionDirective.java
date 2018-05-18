package it.polimi.ingsw.server.compilers.instructions.directives;

import it.polimi.ingsw.core.instructions.Instruction;

import java.io.Serializable;
import java.util.List;

public class InstructionDirective implements Serializable {

    private static final long serialVersionUID = 8582105348106579070L;

    /**
     * The id of the instruction.
     */
    private String id;

    /**
     * The class to instantiate to execute the instruction.
     */
    private Class<? extends Instruction> targetClass;

    /**
     * The directives to instantiate the parameters.
     */
    private List<InstructionParameterDirective> parameterDirectives;

    /**
     * The directives to instantiate the exposed variables.
     */
    private List<InstructionExposedVariableDirective> exposedVariableDirectives;

    /**
     * @return the id of the instruction
     */
    public String getId() {
        return id;
    }

    /**
     * @return the class to instantiate to execute the instruction
     */
    public Class<? extends Instruction> getTargetClass() {
        return targetClass;
    }

    /**
     * @return the directives to instantiate the parameters
     */
    public List<InstructionParameterDirective> getParameterDirectives() {
        return parameterDirectives;
    }

    /**
     * @return the directives to instantiate the exposed variables
     */
    public List<InstructionExposedVariableDirective> getExposedVariableDirectives() {
        return exposedVariableDirectives;
    }

    /**
     * @param id the id of the instruction
     * @param targetClass the class to instantiate to execute the instruction
     * @param parameterDirectives the directives to instantiate the parameters
     * @param exposedVariableDirectives the directives to instantiate the exposed variables
     */
    public InstructionDirective(String id, Class<? extends Instruction> targetClass, List<InstructionParameterDirective> parameterDirectives, List<InstructionExposedVariableDirective> exposedVariableDirectives) {
        this.id = id;
        this.targetClass = targetClass;
        this.parameterDirectives = parameterDirectives;
        this.exposedVariableDirectives = exposedVariableDirectives;
    }
}
