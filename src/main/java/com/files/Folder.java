package com.files;

public class Folder {
    private String name;
    private long size;
    private final int id;

    public Folder() {
        id = hashCode();
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

    public void setSize(long size) {
        this.size = size;
    }
}
