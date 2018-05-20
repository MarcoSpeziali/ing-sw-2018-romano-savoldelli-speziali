package it.polimi.ingsw.server.initializers;

import it.polimi.ingsw.server.utils.VariableSupplier;
import it.polimi.ingsw.server.instructions.predicates.Predicate;
import it.polimi.ingsw.server.compilers.commons.CompiledParameter;
import it.polimi.ingsw.server.compilers.instructions.predicates.CompiledPredicate;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.List;

public class PredicateInitializer {
    private PredicateInitializer() { }

    /**
     * Initializes a {@link Predicate} from a {@link CompiledPredicate}.
     * @param compiledPredicate the {@link CompiledPredicate} to instantiate
     * @return an instance of {@link Predicate}
     * @throws NoSuchMethodException if the constructor could not be found
     * @throws IllegalAccessException if this {@code Constructor} object
     *         is enforcing Java language access control and the underlying
     *         constructor is inaccessible
     * @throws InstantiationException if the class that declares the
     *         underlying constructor represents an abstract class
     * @throws InvocationTargetException if the underlying constructor
     *         throws an exception
     */
    public static Predicate instantiate(CompiledPredicate compiledPredicate) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // gets the target class
        Class<? extends Predicate> targetClass = compiledPredicate.getPredicateClass();

        // gets the constructor parameter
        List<CompiledParameter> compiledParameters = compiledPredicate.getParameters();

        if (compiledParameters.isEmpty()) {
            // gets the constructor
            Constructor<?> constructor = targetClass.getConstructor();

            // creates the new instance
            return (Predicate) constructor.newInstance();
        }
        else {
            // gets the constructor
            Constructor<?> constructor = targetClass.getConstructor(
                    compiledParameters.stream()
                            .sorted(Comparator.comparing(CompiledParameter::getPosition))
                            .map(CompiledParameter::getType)
                            .toArray(Class<?>[]::new)
            );

            // creates the new instance
            return (Predicate) constructor.newInstance(
                    compiledParameters.stream()
                            .sorted(Comparator.comparing(CompiledParameter::getPosition))
                            .map(compiledParameter -> compiledParameter.getType().equals(VariableSupplier.class) ?
                                    compiledParameter.getParameterValue() :
                                    compiledParameter.getParameterValue().get(null)
                            ).toArray()
            );
        }
    }
}
