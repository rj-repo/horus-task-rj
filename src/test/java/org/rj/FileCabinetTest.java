package org.rj;

import org.junit.Test;
import org.rj.implementation.FileCabinet;
import org.rj.implementation.MultiFolderProvider;
import org.rj.interfaces.Folder;
import org.rj.interfaces.MultiFolder;

import static org.junit.Assert.*;
import java.util.List;
import java.util.Optional;

public class FileCabinetTest {

    @Test
    public void testFindFolderByName_ExistingFolder() {
        // Given
        List<Folder> folders = new java.util.ArrayList<>();
        folders.add(new SimpleFolder("Personal", "SMALL"));
        folders.add(new SimpleFolder("Work", "MEDIUM"));
        Folder multiFolder = new MultiFolderImpl("Projects", "LARGE", folders);

        Folder multiFolder1 = new MultiFolderImpl("Projects1", "LARGE", List.of(
                new SimpleFolder("Personal1", "SMALL"),
                new SimpleFolder("Work1", "MEDIUM"),
                multiFolder
        ));
        FileCabinet cabinet = new FileCabinet(List.of(
                new SimpleFolder("Documents", "MEDIUM"),
                new SimpleFolder("Pictures", "LARGE"),
                new SimpleFolder("Videos", "LARGE"),
                multiFolder1
        ), new MultiFolderProvider());

        // When
        Optional<Folder> result = cabinet.findFolderByName("Projects");

        // Then
        assertTrue("Folder 'Projects' should be found", result.isPresent());
    }

    @Test
    public void testFindFolderByName_NonExistingFolder() {
        // Given
        FileCabinet cabinet = new FileCabinet(List.of(
                new SimpleFolder("Documents", "MEDIUM"),
                new SimpleFolder("Pictures", "LARGE"),
                new SimpleFolder("Videos", "LARGE")
        ), new MultiFolderProvider());

        // When
        Optional<Folder> result = cabinet.findFolderByName("NonExistent");

        // Then
        assertFalse("Folder 'NonExistent' should not be found", result.isPresent());
    }

    @Test
    public void testFindFoldersBySize_MatchingFolders() {
        // Given
        Folder multiFolder = new MultiFolderImpl("Projects", "LARGE", List.of(
                new SimpleFolder("Personal", "SMALL"),
                new SimpleFolder("Work", "MEDIUM")
        ));
        FileCabinet cabinet = new FileCabinet(List.of(
                new SimpleFolder("Documents", "MEDIUM"),
                new SimpleFolder("Pictures", "LARGE"),
                new SimpleFolder("Videos", "LARGE"),
                multiFolder
        ), new MultiFolderProvider());

        // When
        List<Folder> result = cabinet.findFoldersBySize("LARGE");

        // Then
        assertEquals("Expected 3 LARGE folders", 3, result.size());
        assertTrue("Missing 'Projects' folder", result.stream().anyMatch(f -> f.getName().equals("Projects")));
    }

    @Test
    public void testFindFoldersBySize_NoMatch() {
        // Given
        FileCabinet cabinet = new FileCabinet(List.of(
                new SimpleFolder("Documents", "MEDIUM"),
                new SimpleFolder("Pictures", "LARGE"),
                new SimpleFolder("Videos", "LARGE")
        ), new MultiFolderProvider());

        // When/Then
        assertThrows("No folders should match 'EXTRA_LARGE'",
                IllegalArgumentException.class,
                () -> cabinet.findFoldersBySize("EXTRA_LARGE"));
    }

    @Test
    public void testCount_TotalFolders() {
        // Given
        // Given
        Folder multiFolder = new MultiFolderImpl("Projects", "LARGE", List.of(
                new SimpleFolder("Personal", "SMALL"),
                new SimpleFolder("Work", "MEDIUM")
        ));
        FileCabinet cabinet = new FileCabinet(List.of(
                new SimpleFolder("Documents", "MEDIUM"),
                new SimpleFolder("Pictures", "LARGE"),
                new SimpleFolder("Videos", "LARGE"),
                multiFolder
        ), new MultiFolderProvider());

        // When
        int count = cabinet.count();

        // Then
        assertEquals("Total folder count should be 6", 6, count);
    }

    @Test
    public void testCountComplexHierarchy_TotalFolders() {
        // Given
        Folder multiFolder = new MultiFolderImpl("Projects", "LARGE", List.of(
                new SimpleFolder("Personal", "SMALL"),
                new SimpleFolder("Work", "MEDIUM")
        ));

        Folder multiFolder1 = new MultiFolderImpl("Projects1", "LARGE", List.of(
                new SimpleFolder("Personal1", "SMALL"),
                new SimpleFolder("Work1", "MEDIUM"),
                multiFolder
        ));

        Folder multiFolder2 = new MultiFolderImpl("Projects1", "LARGE", List.of(
                multiFolder1
        ));
        FileCabinet cabinet = new FileCabinet(List.of(
                new SimpleFolder("Documents", "MEDIUM"),
                new SimpleFolder("Pictures", "LARGE"),
                new SimpleFolder("Videos", "LARGE"),
                multiFolder2
        ), new MultiFolderProvider());

        // When
        int count = cabinet.count();

        // Then
        assertEquals("Total folder count should be 6", 10, count);
    }

    @Test
    public void testCountNullStructure() {
        //Given
        FileCabinet cabinet = new FileCabinet(List.of(), new MultiFolderProvider());

        //When
        int count = cabinet.count();

        //Then
        assertEquals("Empty list", 0, count);

    }

    @Test
    public void testCountAsFolder_TotalFolders() {
        // Given
        Folder multiFolder = new MultiFolderImpl("Projects", "LARGE", List.of(
                new SimpleFolder("Personal", "SMALL"),
                new SimpleFolder("Work", "MEDIUM")
        ));
        FileCabinet cabinet = new FileCabinet(List.of(
                new SimpleFolder("Documents", "MEDIUM"),
                new SimpleFolder("Pictures", "LARGE"),
                new SimpleFolder("Videos", "LARGE"),
                multiFolder
        ), new MultiFolderProvider());

        // When
        int count = cabinet.count();

        // Then
        assertEquals("Total folder count should be 6", 6, count);
    }

    /**
     * Sample implementation of a simple folder
     */
    class SimpleFolder implements Folder {
        private final String name;
        private final String size;

        public SimpleFolder(String name, String size) {
            this.name = name;
            this.size = size;
        }

        @Override
        public String getName() { return name; }

        @Override
        public String getSize() { return size; }
    }

    /**
     * Sample implementation of a multi-folder
     */
    class MultiFolderImpl extends SimpleFolder implements MultiFolder {
        private final List<Folder> subFolders;

        public MultiFolderImpl(String name, String size, List<Folder> subFolders) {
            super(name, size);
            this.subFolders = subFolders;
        }

        @Override
        public List<Folder> getFolders() { return subFolders; }
    }

}