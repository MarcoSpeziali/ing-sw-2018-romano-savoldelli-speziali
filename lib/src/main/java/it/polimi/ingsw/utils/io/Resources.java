package it.polimi.ingsw.utils.io;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public final class Resources {
    private Resources() {}

    /**
     * @param callingClass the {@link Class} that is calling this method, used to get the {@link ClassLoader}
     * @param relativePath the path to the resource relative to the {@code resources} folder
     * @return the url which points to the resource
     */
    public static URL getResource(Class<?> callingClass, String relativePath) {
        return getResource(callingClass.getClassLoader(), relativePath);
    }

    /**
     * @param classLoader the {@link ClassLoader} of the {@link Class} calling this method
     * @param relativePath the path to the resource relative to the {@code resources} folder
     * @return the url which points to the resource
     */
    public static URL getResource(ClassLoader classLoader, String relativePath) {
        try {
            String[] tokens = relativePath.split("/");

            if (tokens.length == 0) {
                return classLoader.getResource(relativePath);
            }

            String firstPath = tokens[0];

            URL resource = classLoader.getResource(firstPath);
            String reassembled = reassemblePath(tokens);

            return new URL(resource, reassembled);
        }
        catch (MalformedURLException ignored) {
            return null;
        }
    }

    private static String reassemblePath(String[] tokens) {
        tokens = Arrays.copyOfRange(tokens, 1, tokens.length);

        return String.join("/", tokens);
    }
}
