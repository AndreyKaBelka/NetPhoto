package com.files;

import explorer.FolderParametrs;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Folder implements Serializable {
    private String name;
    private long size;
    private final int id;
    private ArrayList<FolderParametrs.Object> files;//Либо папки либо файлы

    public Folder(File dir, String name) {
        FolderParametrs folderParametrs = new FolderParametrs();
        folderParametrs.createTheTree(dir, name);
        size = folderParametrs.getSize();
        files = folderParametrs.getListOfFiles();
        this.name = name;
        id = hashCode();
    }

    public Folder(String name, int id) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String print(Folder folder) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (FolderParametrs.Object object : folder.getFiles()) {
            if (object.isFolder()) {
                stringBuilder.append(object.folder.toString());
            } else {
                stringBuilder.append(object.photo);
            }
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    public ArrayList<FolderParametrs.Object> getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return "Name: " + name + " Size: " + size + " ID: " + id + "\n" + print(this);
    }
}
