package it.polimi.ingsw.server.initializers;

import it.polimi.ingsw.server.Objective;
import it.polimi.ingsw.server.compilers.instructions.CompiledInstruction;
import it.polimi.ingsw.server.compilers.objectives.CompiledObjective;
import it.polimi.ingsw.server.instructions.Instruction;
import it.polimi.ingsw.utils.streams.StreamExceptionWrapper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectiveInitializer {
    private ObjectiveInitializer() {
    }

    /**
     * @param compiledObjective the {@link CompiledObjective} to instantiate
     * @return an instance of {@link Objective}
     * @throws NoSuchMethodException     if the constructor could not be found
     * @throws IllegalAccessException    if this {@code Constructor} object
     *                                   is enforcing Java language access control and the underlying
     *                                   constructor is inaccessible
     * @throws InstantiationException    if the class that declares the
     *                                   underlying constructor represents an abstract class
     * @throws InvocationTargetException if the underlying constructor
     *                                   throws an exception
     */
    public static Objective instantiate(CompiledObjective compiledObjective) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return new Objective(
                compiledObjective.getPointsPerCompletion(),
                compiledObjective.getDescription(),
                instantiateInstructions(compiledObjective.getInstructions())
        );
    }

    /**
     * @param compiledInstructions a {@link List} of {@link CompiledInstruction} to instantiate
     * @return a {@link List} of {@link Instruction}
     * @throws NoSuchMethodException     if the constructor could not be found
     * @throws IllegalAccessException    if this {@code Constructor} object
     *                                   is enforcing Java language access control and the underlying
     *                                   constructor is inaccessible
     * @throws InstantiationException    if the class that declares the
     *                                   underlying constructor represents an abstract class
     * @throws InvocationTargetException if the underlying constructor
     *                                   throws an exception
     */
    private static List<Instruction> instantiateInstructions(List<CompiledInstruction> compiledInstructions) throws NoSuchMethodException, InstantiationException, InvocationTargetException, IllegalAccessException {
        try {
            return compiledInstructions.stream()
                    .map(compiledInstruction -> {
                        // due to the strict (and terrible, imo) exception handling of java
                        // the possible exceptions generated by InstructionInitializer::instantiate
                        // cannot be easily propagated
                        try {
                            return InstructionInitializer.instantiate(compiledInstruction);
                        }
                        catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                            return StreamExceptionWrapper.wrap(e);
                        }
                    }).collect(Collectors.toList());
        }
        catch (StreamExceptionWrapper e) {
            return e.tryUnwrap(NoSuchMethodException.class)
                    .tryUnwrap(InstantiationException.class)
                    .tryUnwrap(InvocationTargetException.class)
                    .tryFinalUnwrap(IllegalAccessException.class);
        }
    }
}
