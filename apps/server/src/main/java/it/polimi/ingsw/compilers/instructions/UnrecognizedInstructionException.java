package it.polimi.ingsw.compilers.instructions;

public class UnrecognizedInstructionException extends RuntimeException {
    public UnrecognizedInstructionException(String instructionId) {
        super("Unrecognized instruction " + instructionId);
    }
}
