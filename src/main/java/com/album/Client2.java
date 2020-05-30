package com.album;

import com.files.Folder;
import com.files.Photo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Client2 {
    private static ArrayList<Folder> folderArrayList = new ArrayList<>();
    private static int lastSize = 0;

    public static void downloadFiles(String dir, Photo photo) throws IOException {
        String fileName = photo.getName();
        if (!(new File(dir).exists())) {
            File createdir = new File(dir);
            if (!createdir.mkdir()) {
                System.out.println("Ошибка!");
                return;
            }
        }
        File fileTemp = new File(dir + "\\" + fileName);
        FileOutputStream fos = new FileOutputStream(fileTemp);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(photo.getByteArray());
        bos.close();
    }

    public static void addNewFolder(Folder folder) {
        folderArrayList.add(folder);
    }

    public static boolean sizeChangedListener() {
        if (folderArrayList.size() != lastSize) {
            lastSize = folderArrayList.size();
            lastSize++;
            return true;
        }
        return false;
    }

    public static Folder getLastFolder() {
        return folderArrayList.get(folderArrayList.size() - 1);
    }
}
