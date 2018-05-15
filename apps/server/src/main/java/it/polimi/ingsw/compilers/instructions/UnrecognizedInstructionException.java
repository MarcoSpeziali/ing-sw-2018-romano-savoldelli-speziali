package it.polimi.ingsw.compilers.instructions;

public class UnrecognizedInstructionException extends RuntimeException {
    private static final long serialVersionUID = 377230700298979955L;

    public UnrecognizedInstructionException(String instructionId) {
        super("Unrecognized instruction " + instructionId);
    }
}
