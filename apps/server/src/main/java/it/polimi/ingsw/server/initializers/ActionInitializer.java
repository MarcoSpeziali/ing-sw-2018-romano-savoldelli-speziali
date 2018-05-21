package it.polimi.ingsw.server.initializers;

import it.polimi.ingsw.server.actions.Action;
import it.polimi.ingsw.server.actions.ActionData;
import it.polimi.ingsw.server.actions.ExecutableAction;
import it.polimi.ingsw.server.compilers.actions.CompiledAction;
import it.polimi.ingsw.server.compilers.commons.CompiledParameter;
import it.polimi.ingsw.server.utils.VariableSupplier;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ActionInitializer {
    private ActionInitializer() {}

    /**
     * Instantiate an {@link Action} from a {@link CompiledAction}.
     * @return an instance of {@link Action}  created from a {@link CompiledAction}
     * @throws NoSuchMethodException if the constructor could not be found
     * @throws IllegalAccessException if this {@code Constructor} object
     *         is enforcing Java language access control and the underlying
     *         constructor is inaccessible
     * @throws InstantiationException if the class that declares the
     *         underlying constructor represents an abstract class
     * @throws InvocationTargetException if the underlying constructor
     *         throws an exception
     */
    public static Action instantiate(CompiledAction compiledAction) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // gets the class to instantiate
        Class<? extends ExecutableAction> targetClass = compiledAction.getClassToInstantiate();

        List<Class<?>> parametersClasses = new LinkedList<>();
        List<Object> parametersValues = new LinkedList<>();

        // the ActionData parameter is always present
        parametersClasses.add(ActionData.class);
        parametersValues.add(compiledAction.getActionData());

        // gets the compiled parameters
        List<CompiledParameter> compiledParameters = compiledAction.getParameters();

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

        // gets the constructor
        Constructor<?> constructor = targetClass.getConstructor(
                parametersClasses.toArray(new Class[0])
        );

        // creates a new instance
        return (Action) constructor.newInstance(
                parametersValues.toArray()
        );
    }
}
