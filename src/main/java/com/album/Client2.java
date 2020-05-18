package com.album;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client2 {

    private Socket socket;

    public void connect(String IP) throws IOException {
        socket = new Socket(InetAddress.getByName("100.73.46.188"), 21);
        PrintStream ps = new PrintStream(socket.getOutputStream( ));
        ps.println("P1");

    }

    public void download(String dir) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        DataInputStream dis = new DataInputStream(bis);
        int filesCount = dis.readInt();
        File[] files = new File[filesCount];
        System.out.println(filesCount);
        for(int i = 0; i < filesCount; i++)
        {
            long fileLength = dis.readLong();
            String fileName = dis.readUTF();
            files[i] = new File(dir + "/" + fileName);
            FileOutputStream fos = new FileOutputStream(files[i]);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            for(int j = 0; j < fileLength; j++) bos.write(bis.read());
            bos.close();
        }
        dis.close();
    }

    public void download_Temp(String dir) throws IOException {
        String dirPath = dir;
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        DataInputStream dis = new DataInputStream(bis);
        int filesCount = dis.readInt();
        File[] files = new File[filesCount];
        JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
        jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpegParams.setCompressionQuality(0.9f);
        ImageIO.setUseCache(false);
        final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        BufferedImage capture;
        for(int i = 0; i < filesCount; i++)
        {
            long fileLength = dis.readLong();
            String fileName = dis.readUTF();
            byte[] byteArray = new byte[(int) fileLength];
            files[i] = new File(dirPath + "/" + fileName);
            writer.setOutput(new FileImageOutputStream(files[i]));
            for(int j = 0; j < fileLength; j++) {
                byteArray[j] = dis.readByte();
            }
            capture = ImageIO.read(new ByteArrayInputStream(byteArray));
            writer.write(null, new IIOImage(capture, null, null), jpegParams);
        }
        dis.close();
    }
}
