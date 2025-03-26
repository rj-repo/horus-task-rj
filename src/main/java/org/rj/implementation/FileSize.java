package org.rj.implementation;

public enum FileSize {

    SMALL("SMALL"),
    MEDIUM("MEDIUM"),
    LARGE("LARGE");

    public final String size;

    FileSize(String size) {
        this.size = size;
    }

    public static void validateSize(String size){
        for(FileSize fs : FileSize.values()){
            if(fs.size.equals(size)){
                return;
            }
        }
        throw new IllegalArgumentException("Size " + size + " is not supported");
    }
}
