package it.polimi.ingsw.utils.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FilesUtils {

    private FilesUtils() {}

    /**
     * Gets a {@link List} {@link List} containing the name of the files in the
     * directory at the provided path.
     * @param filePath the path to target directory
     * @return {@code null} if the path does not denote a directory, a {@link List} containing
     *         the name of the files in the directory at the provided path
     */
    public static List<String> listFilesInDirectory(String filePath) {
        return listFilesInDirectory(new File(filePath));
    }

    /**
     * Gets a {@link List} {@link List} containing the name of the files in the
     * directory at the provided path.
     * @param targetDirectory the target directory
     * @return {@code null} if the path does not denote a directory, a {@link List} containing
     *         the name of the files in the directory at the provided path
     */
    @SuppressWarnings("squid:S1168")
    public static List<String> listFilesInDirectory(File targetDirectory) {
        // gets the files in the directory
        File[] files = targetDirectory.listFiles();

        // if null is returned the file is not a directory
        if (files == null) {
            return null;
        }

        return Arrays.stream(files)
                .map(File::getName)
                .collect(Collectors.toList());
    }

    /**
     * @param filePath the file path
     * @return the last modified timestamp of the file
     */
    public static long getLastModifiedOfFile(String filePath) {
        return new File(filePath).lastModified();
    }

    public static long getLastModifiedOfFile(URL url) {
        try {
            return url.openConnection().getLastModified();
        } catch (IOException e) {
            return 0L;
        }
    }

    public static void deleteDirectoryRecursively(File file) {
        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            File[] children = file.listFiles();

            if (children == null) {
                return;
            }

            if (children.length == 0) {
                 file.delete();
            }
            else {
                for (File child : children) {
                    deleteDirectoryRecursively(child);
                }
            }

            file.delete();
        }
        else {
            file.delete();
        }
    }
}
