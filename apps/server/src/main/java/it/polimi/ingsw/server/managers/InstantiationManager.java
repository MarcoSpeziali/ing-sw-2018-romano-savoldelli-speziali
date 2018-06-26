package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.models.ObjectiveCard;
import it.polimi.ingsw.models.ToolCard;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.server.Constants;
import it.polimi.ingsw.server.compilers.cards.CompiledObjectiveCard;
import it.polimi.ingsw.server.instantiators.CardInstantiator;
import it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;

import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.wrap;

// TODO: docs and optimize for multiple matches (InMemoryCache)
public final class InstantiationManager {
    private InstantiationManager() {}

    public static synchronized Window[] instantiateWindows() throws IOException, ClassNotFoundException {
        return deserializeObjects(
                getPathForResource(Constants.Resources.WINDOWS),
                Window[].class
        );
    }
    
    public static synchronized ObjectiveCard[] instantiatePublicObjectiveCards() throws IOException, ClassNotFoundException {
        CompiledObjectiveCard[] compiledObjectiveCards = deserializeObjects(
                getPathForResource(Constants.Resources.PUBLIC_CARDS),
                CompiledObjectiveCard[].class
        );
        
        try {
            return Arrays.stream(compiledObjectiveCards)
                    .map(wrap((FunctionalExceptionWrapper.UnsafeFunction<CompiledObjectiveCard, ObjectiveCard>) CardInstantiator::instantiate))
                    .toArray(ObjectiveCard[]::new);
        }
        catch (FunctionalExceptionWrapper e) {
            return e.tryUnwrap(IOException.class).tryFinalUnwrap(ClassNotFoundException.class);
        }
    }
    
    public static synchronized ObjectiveCard[] instantiatePrivateObjectiveCards() throws IOException, ClassNotFoundException {
        CompiledObjectiveCard[] compiledObjectiveCards = deserializeObjects(
                getPathForResource(Constants.Resources.PRIVATE_CARDS),
                CompiledObjectiveCard[].class
        );
    
        try {
            return Arrays.stream(compiledObjectiveCards)
                    .map(wrap((FunctionalExceptionWrapper.UnsafeFunction<CompiledObjectiveCard, ObjectiveCard>) CardInstantiator::instantiate))
                    .toArray(ObjectiveCard[]::new);
        }
        catch (FunctionalExceptionWrapper e) {
            return e.tryUnwrap(IOException.class).tryFinalUnwrap(ClassNotFoundException.class);
        }
    }
    
    public static synchronized ToolCard[] instantiateToolCards() throws IOException, ClassNotFoundException {
        return deserializeObjects(
                getPathForResource(Constants.Resources.TOOL_CARDS),
                ToolCard[].class
        );
    }

    private static synchronized String getPathForResource(Constants.Resources resource) {
        return Paths.get(
                Constants.Paths.COMPILATION_FOLDER.toString(),
                String.valueOf(CompilationManager.lastCompilation()),
                resource.toString().toLowerCase()
        ).toString();
    }

    /**
     * Deserialize an array from a file.
     *
     * @param filePath the path to the file
     * @param <T>      the type of the serialized object
     * @return an array of type {@code T} containing the deserialized objects
     * @throws IOException            if any IO exception occurs
     * @throws ClassNotFoundException if a class could not be found
     */
    private static synchronized <T> T[] deserializeObjects(String filePath, Class<T[]> destinationClass) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            try (ObjectInputStream in = new ObjectInputStream(fis)) {

                Serializable[] deserialized = (Serializable[]) in.readObject();

                return Arrays.copyOf(deserialized, deserialized.length, destinationClass);
            }
        }
    }
}
