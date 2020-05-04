package com.album;

import javax.imageio.*;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private Socket socket;

    public void connect(String IP) throws IOException {
        socket = new Socket(IP, 8030);
    }

    public void download(String dir) throws IOException {

        String dirPath = dir;

        //socket = new Socket(InetAddress.getLocalHost(), 8030);
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        DataInputStream dis = new DataInputStream(bis);

        int filesCount = dis.readInt();
        File[] files = new File[filesCount];
        JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
        jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpegParams.setCompressionQuality(0.9f);
        final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        BufferedImage capture;
        ImageIO.setUseCache(false);
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
