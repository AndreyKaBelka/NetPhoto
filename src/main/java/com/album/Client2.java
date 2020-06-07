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

    public static void downloadFiles(String dir, Photo photo, int cnt_photo) throws IOException {
        String fileName = photo.getName();
        int cnt = 1;
        String newDir = dir;
        if (cnt_photo == 1) {
            if (!(new File(newDir).exists())) {
                File createdir = new File(newDir);
                if (!createdir.mkdir()) {
                    System.out.println("Ошибка!");
                    return;
                }
            } else {
                while (true) {
                    newDir = dir;
                    newDir = newDir + " (" + cnt + ")";
                    if (new File(newDir).exists()) {
                        cnt++;
                    } else {
                        File createdDir = new File(newDir);
                        if (!createdDir.mkdir()) {
                            System.out.println("Ошибка!");
                            return;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        File fileTemp = new File(newDir + "\\" + fileName);
        FileOutputStream fos = new FileOutputStream(fileTemp);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(photo.getByteArray());
        bos.close();
    }

    public static void addNewFolder(Folder folder) {
        folderArrayList.add(folder);
    }

    static boolean sizeChangedListener() {
        if (folderArrayList.size() != lastSize) {
            lastSize = folderArrayList.size();
            lastSize++;
            return true;
        }
        return false;
    }

    static Folder getLastFolder() {
        return folderArrayList.get(folderArrayList.size() - 1);
    }
}
