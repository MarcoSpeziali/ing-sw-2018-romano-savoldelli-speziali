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

    /**
     * @param url the file url
     * @return the last modified timestamp of the file
     */
    public static long getLastModifiedOfFile(URL url) {
        try {
            return url.openConnection().getLastModified();
        } catch (IOException e) {
            return 0L;
        }
    }

    /**
     * @param file the file or the directory to delete
     * @return {@code true} if the file has been deleted correctly, {@code false} otherwise
     */
    @SuppressWarnings("squid:S4042")
    public static boolean deleteDirectoryRecursively(File file) {
        // if the file does not exists true is returned
        if (!file.exists()) {
            return true;
        }

        boolean result = true;

        // if the file is a directory every file in it must be deleted first
        if (file.isDirectory()) {
            File[] children = file.listFiles();

            // if children is null then the file does not exists
            if (children == null) {
                return true;
            }

            // if the directory has no children: the directory is deleted and true is returned
            if (children.length == 0) {
                result = file.delete();
            }
            else {
                // if the directory has some children they get deleted recursively
                for (File child : children) {
                    result &= deleteDirectoryRecursively(child);
                }
            }

            // finally the directory can be deleted
            result &= file.delete();
        }
        else {
            // otherwise it can be deleted immediately
            result = file.delete();
        }

        return result;
    }
}
