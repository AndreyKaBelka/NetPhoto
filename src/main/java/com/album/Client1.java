package com.album;

import com.files.Photo;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.ArrayList;

public class Client1 {

    public static float compressionQuality = 1f;

    public static ArrayList<Photo> getCompressedFiles(String dir) throws IOException {
        ArrayList<Photo> photos = new ArrayList<>();

        System.out.println(compressionQuality);

        File[] files = new File(dir).listFiles();
        ByteArrayOutputStream bStream;

        for (File file : files) {
            Photo photo = new Photo();
            if (compressionQuality != 1f) {
                bStream = new ByteArrayOutputStream();
                ImageOutputStream sos = new MemoryCacheImageOutputStream(bStream);
                ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
                ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
                jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                jpgWriteParam.setCompressionQuality(compressionQuality);
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
                photos.add(photo);
                sos.flush();
                bis.close();
            } else {
                System.out.println("НЕ СЖИМАЕМ!");
                BufferedImage bufferedImage = ImageIO.read(file);

                WritableRaster raster = bufferedImage.getRaster();
                DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
                photo.setByteArray(data.getData());
                photo.setName(file.getName());
                photo.setSize(data.getData().length);
                photos.add(photo);
            }
        }

        return photos;
    }
}
