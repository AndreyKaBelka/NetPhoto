package com.album;

import com.files.Photo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Client2 {
    public void downloadFiles(String dir, ArrayList<Photo> photos) throws IOException {
        for(Photo photo: photos)
        {
            long fileLength = photo.getSize();
            String fileName = photo.getName();
            File fileTemp = new File(dir + "/" + fileName);
            FileOutputStream fos = new FileOutputStream(fileTemp);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            for(int j = 0; j < fileLength; j++) bos.write(photo.getByteArray());
            bos.close();
        }
    }
}
