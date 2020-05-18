package com.album;

import com.files.Photo;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Client1 {

    public ArrayList<Photo> getCompressedFiles(String dir) throws IOException {
        ArrayList<Photo> photos = new ArrayList<>();

        File[] files = new File(dir).listFiles();
        ByteArrayOutputStream bStream;

        for(File file : files)
        {
            Photo photo = new Photo();
            bStream = new ByteArrayOutputStream();
            ImageOutputStream sos = new MemoryCacheImageOutputStream(bStream);
            ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
            ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
            jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpgWriteParam.setCompressionQuality(0.5f);
            jpgWriter.setOutput(sos);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            BufferedImage capture = ImageIO.read(file);
            IIOImage outputImage = new IIOImage(capture, null, null);
            jpgWriter.write(null, outputImage, jpgWriteParam);
            photo.setSize(bStream.size());
            String name = file.getName();
            photo.setName(name);
            photo.setByteArray(bStream.toByteArray());
            sos.flush();
            bis.close();
        }

        return photos;
    }
}
