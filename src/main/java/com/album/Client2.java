package com.album;

import com.files.Photo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Client2 {
    public static void downloadFiles(String dir, Photo photo) throws IOException {
        String fileName = photo.getName();
        File fileTemp = new File(dir + "/" + fileName);
        FileOutputStream fos = new FileOutputStream(fileTemp);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(photo.getByteArray());
        bos.close();
        System.out.println("Я сделал фото сука!");
    }
}
