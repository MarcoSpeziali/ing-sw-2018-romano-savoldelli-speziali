package it.polimi.ingsw.compilers.instructions;

import it.polimi.ingsw.compilers.commons.CompiledParameter;
import it.polimi.ingsw.compilers.instructions.predicates.CompiledPredicate;
import it.polimi.ingsw.core.instructions.Instruction;

import java.io.Serializable;
import java.util.List;

public class CompiledInstruction implements Serializable {

    private static final long serialVersionUID = -6341157506502027555L;

    /**
     * The instruction id.
     */
    private String instructionId;

    /**
     * The class of the instruction.
     */
    private Class<? extends Instruction> instructionClass;

    /**
     * The sub-instructions.
     */
    private List<CompiledInstruction> subInstructions;

    /**
     * The parameters.
     */
    private List<CompiledParameter> parameters;

    /**
     * The exposed variables.
     */
    private List<CompiledExposedVariable> exposedVariables;

    /**
     * The predicates.
     */
    private List<CompiledPredicate> predicates;

    /**
     * @return the instruction id
     */
    public String getInstructionId() {
        return instructionId;
    }

    /**
     * @return the class of the instruction
     */
    public Class<? extends Instruction> getInstructionClass() {
        return instructionClass;
    }

    /**
     * @return the sub-instructions
     */
    public List<CompiledInstruction> getSubInstructions() {
        return subInstructions;
    }

    /**
     * @return the parameters
     */
    public List<CompiledParameter> getParameters() {
        return parameters;
    }

    /**
     * @return the exposed variables
     */
    public List<CompiledExposedVariable> getExposedVariables() {
        return exposedVariables;
    }

    /**
     * @return the predicates
     */
    public List<CompiledPredicate> getPredicates() {
        return predicates;
    }

    /**
     * @param instructionId the instruction id
     * @param instructionClass the class of the instruction
     * @param subInstructions the sub-instructions
     * @param parameters the parameters
     * @param exposedVariables the exposed variables
     * @param predicates the predicates
     */
    public CompiledInstruction(String instructionId, Class<? extends Instruction> instructionClass, List<CompiledInstruction> subInstructions, List<CompiledParameter> parameters, List<CompiledExposedVariable> exposedVariables, List<CompiledPredicate> predicates) {
        this.instructionId = instructionId;
        this.instructionClass = instructionClass;
        this.subInstructions = subInstructions;
        this.parameters = parameters;
        this.exposedVariables = exposedVariables;
        this.predicates = predicates;
    }
}
