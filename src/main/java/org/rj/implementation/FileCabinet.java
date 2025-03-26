package org.rj.implementation;

import org.rj.interfaces.Cabinet;
import org.rj.interfaces.Folder;
import org.rj.interfaces.MultiCabinet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileCabinet implements Cabinet {
    private final List<Folder> folders;
    private final MultiCabinet multiCabinet;

    public FileCabinet(List<Folder> folders, MultiCabinet multiCabinet) {
        this.folders = folders;
        this.multiCabinet = multiCabinet;
    }

    @Override
    public Optional<Folder> findFolderByName(String name) {
        List<Folder> allFound = getAllFolders(folders).stream()
                .filter(el -> el.getName().equals(name))
                .toList();

        if (allFound.size() > 1) {
            throw new DuplicatedFolderException("Found multiple folders with the same name: " + name);
        }

        return allFound.stream().findFirst();
    }

    @Override
    public List<Folder> findFoldersBySize(String size) {
        FileSize.validateSize(size);
        return getAllFolders(folders).stream()
                .filter(el -> el.getSize().equals(size))
                .toList();
    }

    @Override
    public int count() {
        return getAllFolders(folders).size();
    }

    private List<Folder> getAllFolders(List<Folder> rootFolders) {
        List<Folder> allFolders = new ArrayList<>();

        for (Folder folder : rootFolders) {
            allFolders.add(folder);
            List<Folder> subFolders = getAllFolders(multiCabinet.getSubFolder(folder));
            allFolders.addAll(subFolders);
        }
        return allFolders;
    }

}
