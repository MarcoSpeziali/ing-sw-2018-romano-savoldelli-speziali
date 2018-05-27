package it.polimi.ingsw.utils.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

class FilesUtilsTest {

    @Test
    void testListFilesInDirectory() {
        List<String> actualFiles = List.of(
                "file_1",
                "file_2",
                "file_3"
        );

        List<String> found = FilesUtils.listFilesInDirectory(
                Objects.requireNonNull(getClass().getClassLoader().getResource("FilesUtilsResources")).getPath()
        );

        found.forEach(s -> Assertions.assertTrue(actualFiles.contains(s)));
    }

    @Test
    void testListFilesInDirectory1() {
        List<String> actualFiles = List.of(
                "file_1",
                "file_2",
                "file_3"
        );

        List<String> found = FilesUtils.listFilesInDirectory(
                Objects.requireNonNull(getClass().getClassLoader().getResource("FilesUtilsResources")).getFile()
        );

        found.forEach(s -> Assertions.assertTrue(actualFiles.contains(s)));
    }

    @Test
    void testGetLastModifiedOfFile() {
        String filePath = Objects.requireNonNull(
                getClass().getClassLoader().getResource("FilesUtilsResources/file_1")
        ).getPath();

        long lastModified = FilesUtils.getLastModifiedOfFile(filePath);

        Assertions.assertEquals(new File(filePath).lastModified(), lastModified);
    }

    @Test
    void testGetLastModifiedOfFile1() throws IOException {
        URL fileURL = Objects.requireNonNull(
                getClass().getClassLoader().getResource("FilesUtilsResources/file_1")
        );

        long lastModified = FilesUtils.getLastModifiedOfFile(fileURL);

        Assertions.assertEquals(fileURL.openConnection().getLastModified(), lastModified);
    }
}