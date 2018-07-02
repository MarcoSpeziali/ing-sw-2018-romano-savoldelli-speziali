package it.polimi.ingsw.utils.io;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class Resources {
    private Resources() {
    }

    /**
     * @param classLoader  the {@link ClassLoader} of the {@link Class} calling this method
     * @param relativePath the path to the resource relative to the {@code resources} folder
     * @return the url which points to the resource
     */
    public static URL getResource(ClassLoader classLoader, String relativePath) {
        /*
         * The problem here is that: classLoader.getResource("folder/../resource.xyz") works only in "test mode" (JUnit),
         * but it does not work in others modes. A workaround for "run mode" is to get the url for the first folder and
         * create an other url by appending the rest of the path to the first url. This works in "run mode" but not in "test
         * mode". So, since the target directory for the "test mode" is "test-classes", in "test mode" the resource is retrieved
         * using classLoader.getResource("folder/../resource.xyz").
         */
        
        try {
            // splits the relative path by '/'
            String[] tokens = relativePath.split("/");

            // if the path points directly to a file then the resource can retrieved normally
            if (tokens.length == 0) {
                return classLoader.getResource(relativePath);
            }

            String firstPath = tokens[0];

            // gets the first folder
            URL resource = classLoader.getResource(firstPath);

            if (resource == null) {
                return null;
            }

            // in "test mode" this method does not work, so the standard one can be used
            if (resource.toString().contains("test-classes")) {
                return classLoader.getResource(relativePath);
            }

            // the path gets reassembles by skipping the first folder
            String reassembled = reassemblePath(tokens);

            // appends the reassembled path to the url of the first folder
            return new URL(resource, reassembled);
        }
        catch (MalformedURLException ignored) {
            return null;
        }
    }

    /**
     * Reassembles the path skipping the first token.
     *
     * @param tokens the splitted path
     * @return the reassembled path with the first token skipped
     */
    private static String reassemblePath(String[] tokens) {
        tokens = Arrays.copyOfRange(tokens, 1, tokens.length);

        return String.join("/", tokens);
    }
}
