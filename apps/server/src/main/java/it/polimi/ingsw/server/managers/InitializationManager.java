package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.server.Constants;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;

public final class InitializationManager {
    private InitializationManager() {}

    public static synchronized Window[] initializeWindows() throws IOException, ClassNotFoundException {
        return deserializeObjects(
                getPathForResource(Constants.Resources.WINDOWS),
                Window[].class
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
