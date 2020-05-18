package com.files;

import java.io.Serializable;

public class Photo implements Serializable {
    private String name;
    private long size;
    private byte[] byteArray;
    private final int id;

    public Photo() {
        id = hashCode();
    }

    public int getId() {
        return id;
    }

    public long getSize() {
        return size;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public String getName() {
        return name;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nSize: " + size + "\n";
    }
}
