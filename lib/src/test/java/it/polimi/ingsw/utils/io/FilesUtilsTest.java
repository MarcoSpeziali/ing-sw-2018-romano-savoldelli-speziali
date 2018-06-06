package it.polimi.ingsw.utils.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        Assertions.assertNull(FilesUtils.listFilesInDirectory(
                Objects.requireNonNull(getClass().getClassLoader().getResource("FilesUtilsResources/file_1")).getPath()
        ));
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

        Assertions.assertNull(FilesUtils.listFilesInDirectory(
                Objects.requireNonNull(getClass().getClassLoader().getResource("FilesUtilsResources/file_1")).getFile()
        ));
    }

    @Test
    void testGetLastModifiedOfFile() {
        String filePath = Objects.requireNonNull(
                getClass().getClassLoader().getResource("FilesUtilsResources/file_1")
        ).getPath();

        long lastModified = FilesUtils.getLastModifiedOfFile(filePath);

        Assertions.assertEquals(new File(filePath).lastModified(), lastModified);

        Assertions.assertEquals(
                0L,
                FilesUtils.getLastModifiedOfFile(
                        Paths.get("file").toString()
                )
        );
    }

    @Test
    void testGetLastModifiedOfFile1() throws IOException {
        URL fileURL = Objects.requireNonNull(
                getClass().getClassLoader().getResource("FilesUtilsResources/file_1")
        );

        long lastModified = FilesUtils.getLastModifiedOfFile(fileURL);

        Assertions.assertEquals(fileURL.openConnection().getLastModified(), lastModified);

        Assertions.assertEquals(
                0L,
                FilesUtils.getLastModifiedOfFile(
                        new URL("file://74189")
                )
        );
    }

    @Test
    void testDeleteDirectoryRecursivelyNotExisting() {
        File directory = mock(File.class);
        when(directory.exists()).thenReturn(false);

        Assertions.assertTrue(FilesUtils.deleteDirectoryRecursively(directory));
    }

    @Test
    void testDeleteDirectoryRecursivelyNotDirectory() {
        File directory = mock(File.class);
        when(directory.exists()).thenReturn(true);
        when(directory.isDirectory()).thenReturn(false);
        when(directory.delete()).thenReturn(true);

        Assertions.assertTrue(FilesUtils.deleteDirectoryRecursively(directory));
    }

    @Test
    void testDeleteDirectoryRecursivelyEmptyDirectory() {
        File directory = mock(File.class);
        when(directory.exists()).thenReturn(true);
        when(directory.isDirectory()).thenReturn(true);
        when(directory.listFiles()).thenReturn(new File[0]);
        when(directory.delete()).thenReturn(true);

        Assertions.assertTrue(FilesUtils.deleteDirectoryRecursively(directory));
    }

    @Test
    void testDeleteDirectoryRecursivelyNullDirectory() {
        File directory = mock(File.class);
        when(directory.exists()).thenReturn(true);
        when(directory.isDirectory()).thenReturn(true);
        when(directory.listFiles()).thenReturn(null);
        when(directory.delete()).thenReturn(true);

        Assertions.assertTrue(FilesUtils.deleteDirectoryRecursively(directory));
    }

    @Test
    void testDeleteDirectoryRecursivelyNotEmptyDirectory() {
        File subFile = mock(File.class);
        when(subFile.exists()).thenReturn(true);
        when(subFile.isDirectory()).thenReturn(false);
        when(subFile.delete()).thenReturn(true);

        File directory = mock(File.class);
        when(directory.exists()).thenReturn(true);
        when(directory.isDirectory()).thenReturn(true);
        when(directory.listFiles()).thenReturn(new File[] { subFile, subFile, subFile });
        when(directory.delete()).thenReturn(true);

        Assertions.assertTrue(FilesUtils.deleteDirectoryRecursively(directory));
    }
}