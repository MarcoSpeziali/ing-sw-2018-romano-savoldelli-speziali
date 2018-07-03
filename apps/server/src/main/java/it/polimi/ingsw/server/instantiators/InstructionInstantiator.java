package it.polimi.ingsw.server.instantiators;

import it.polimi.ingsw.server.compilers.commons.CompiledParameter;
import it.polimi.ingsw.server.compilers.instructions.CompiledExposedVariable;
import it.polimi.ingsw.server.compilers.instructions.CompiledInstruction;
import it.polimi.ingsw.server.compilers.instructions.predicates.CompiledPredicate;
import it.polimi.ingsw.server.instructions.Instruction;
import it.polimi.ingsw.server.instructions.predicates.Predicate;
import it.polimi.ingsw.server.utils.VariableSupplier;
import it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InstructionInstantiator {
    private InstructionInstantiator() {
    }

    /**
     * Instantiates an {@link Instruction} from a {@link CompiledInstruction}.
     *
     * @param compiledInstruction the compiled instruction
     * @return an instance of {@link Instruction} created from a {@link CompiledInstruction}
     * @throws NoSuchMethodException     if the constructor could not be found
     * @throws IllegalAccessException    if this {@code Constructor} object
     *                                   is enforcing Java language access control and the underlying
     *                                   constructor is inaccessible
     * @throws InstantiationException    if the class that declares the
     *                                   underlying constructor represents an abstract class
     * @throws InvocationTargetException if the underlying constructor
     *                                   throws an exception
     */
    public static Instruction instantiate(CompiledInstruction compiledInstruction) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // gets the target class
        Class<? extends Instruction> targetClass = compiledInstruction.getInstructionClass();

        List<Class<?>> parametersClasses = new LinkedList<>();
        List<Object> parametersValues = new LinkedList<>();

        // gets the exposed variables
        Map<String, String> exposedVariables = compiledInstruction.getExposedVariables().stream()
                .collect(Collectors.toMap(
                        CompiledExposedVariable::getDefaultName,
                        CompiledExposedVariable::getOverrideValue)
                );

        // if no exposed variable is available they do not contribute in the constructor
        if (!exposedVariables.isEmpty()) {
            parametersClasses.add(Map.class);
            parametersValues.add(exposedVariables);
        }

        Map<String, Predicate> predicateMap = getPredicateMap(compiledInstruction.getPredicates());

        // if no predicate is available they do not contribute in the constructor
        if (!predicateMap.isEmpty()) {
            parametersClasses.add(Map.class);
            parametersValues.add(predicateMap);
        }

        // gets the compiled parameters
        List<CompiledParameter> compiledParameters = compiledInstruction.getParameters();

        // if no parameter is available they do not contribute in the constructor
        if (!compiledParameters.isEmpty()) {
            parametersClasses.addAll(
                    compiledParameters.stream()
                            .sorted(Comparator.comparing(CompiledParameter::getPosition))
                            .map(CompiledParameter::getType)
                            .collect(Collectors.toList())
            );
            parametersValues.addAll(
                    compiledParameters.stream()
                            .sorted(Comparator.comparing(CompiledParameter::getPosition))
                            .map(compiledParameter -> compiledParameter.getType().equals(VariableSupplier.class) ?
                                    compiledParameter.getParameterValue() :
                                    compiledParameter.getParameterValue().get(null)
                            ).collect(Collectors.toList())
            );
        }

        Instruction instruction;

        // creates the actual object with or without parameters
        if (parametersClasses.isEmpty()) {
            Constructor<?> constructor = targetClass.getConstructor();
            instruction = (Instruction) constructor.newInstance();
        }
        else {
            Constructor<?> constructor = targetClass.getConstructor(
                    parametersClasses.toArray(new Class[0])
            );

            instruction = (Instruction) constructor.newInstance(
                    parametersValues.toArray()
            );
        }

        // initialized the sub-instructions
        List<Instruction> instructions = instantiateSubInstructions(compiledInstruction.getSubInstructions());

        // sets the sub-instructions
        instruction.setInstructions(instructions);

        return instruction;
    }

    /**
     * Instantiates the provided predicates.
     *
     * @param predicates the {@link List} of {@link CompiledPredicate} to initialize
     * @return a {@link Map} of {@link String}, which represents the predicate parameter
     * name, and {@link Predicate}, which represents the initialized predicate.
     * @throws NoSuchMethodException     if the constructor could not be found
     * @throws IllegalAccessException    if this {@code Constructor} object
     *                                   is enforcing Java language access control and the underlying
     *                                   constructor is inaccessible
     * @throws InstantiationException    if the class that declares the
     *                                   underlying constructor represents an abstract class
     * @throws InvocationTargetException if the underlying constructor
     *                                   throws an exception
     */
    @SuppressWarnings("squid:S00112") // throw new RuntimeException(e)
    private static Map<String, Predicate> getPredicateMap(List<CompiledPredicate> predicates) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        try {
            return predicates.stream()
                    .collect(Collectors.toMap(CompiledPredicate::getPredicateName, compiledPredicate -> {
                        // due to the strict (and terrible, imo) exception handling of java
                        // the possible exception generated by PredicateInstantiator::instantiate
                        // cannot be easily propagated
                        try {
                            return PredicateInstantiator.instantiate(compiledPredicate);
                        }
                        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                            return FunctionalExceptionWrapper.wrap(e);
                        }
                    }));
        }
        catch (FunctionalExceptionWrapper e) {
            return e.tryUnwrap(NoSuchMethodException.class)
                    .tryUnwrap(IllegalAccessException.class)
                    .tryUnwrap(InvocationTargetException.class)
                    .tryFinalUnwrap(InstantiationException.class);
        }
    }

    /**
     * Instantiates the provided instructions.
     *
     * @param instructions the {@link List} of {@link CompiledInstruction} to initialize
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
    @SuppressWarnings("squid:S00112") // throw new RuntimeException(e)
    private static List<Instruction> instantiateSubInstructions(List<CompiledInstruction> instructions) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // if the instructions list is empty, an empty list is returned
        if (instructions.isEmpty()) {
            return List.of();
        }

        try {
            // maps the compiled instructions into their initialized form
            return instructions.stream()
                    .map(compiledInstruction -> {
                        // due to the strict (and terrible, imo) exception handling of java
                        // the possible exception generated by InstructionInstantiator::initialize
                        // cannot be easily propagated
                        try {
                            return InstructionInstantiator.instantiate(compiledInstruction);
                        }
                        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                            return FunctionalExceptionWrapper.wrap(e);
                        }
                    }).collect(Collectors.toList());
        }
        catch (FunctionalExceptionWrapper e) {
            return e.tryUnwrap(NoSuchMethodException.class)
                    .tryUnwrap(IllegalAccessException.class)
                    .tryUnwrap(InvocationTargetException.class)
                    .tryFinalUnwrap(InstantiationException.class);
        }
    }
}
