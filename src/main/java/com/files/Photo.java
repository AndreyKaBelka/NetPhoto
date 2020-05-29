package com.files;

import java.io.Serializable;
import java.util.UUID;

public class Photo implements Serializable {
    private String name;
    private long size;
    private byte[] byteArray;
    private final String id;

    public Photo() {
        id = UUID.randomUUID().toString();
    }

    public Photo(String name, long size) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.size = size;
    }

    public String getId() {
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
        return "Name: " + name + " Size: " + size + " ID: " + id;
    }
}
