package org.rj.implementation;

import org.rj.interfaces.Folder;
import org.rj.interfaces.MultiCabinet;
import org.rj.interfaces.MultiFolder;

import java.util.ArrayList;
import java.util.List;

public class MultiFolderProvider implements MultiCabinet {

    @Override
    public List<Folder> getSubFolder(Folder folder){
        if(folder instanceof MultiFolder) {
            return  ((MultiFolder) folder).getFolders();
        }
        return new ArrayList<>();
    }
}
