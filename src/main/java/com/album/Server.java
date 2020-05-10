package com.album;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public void server_start(String dir) throws IOException {
        String directory = dir;
        ServerSocket server = new ServerSocket(21);
        File[] files = new File(directory).listFiles();
        Socket socket;
        socket = server.accept();
        //BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
        ImageOutputStream bos = new MemoryCacheImageOutputStream(socket.getOutputStream()); // For example implementations see below
        //DataOutputStream dos = new DataOutputStream((OutputStream) bos);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeInt(files.length);
        for(File file : files)
        {
            //long length = file.length();
            //dos.writeLong(length);
            ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
            ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
            jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpgWriteParam.setCompressionQuality(0.7f);
            jpgWriter.setOutput(bos);
            //String name = file.getName();
            //dos.writeUTF(name);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            int theByte = 0;
            /*while((theByte = bis.read()) != -1) {
                //bos.write(theByte);
            }*/
            BufferedImage capture = ImageIO.read(file);

            IIOImage outputImage = new IIOImage(capture, null, null);
            jpgWriter.write(null, outputImage, jpgWriteParam);
            //jpgWriter.dispose();
            bis.close();
        }
        //jpgWriter.dispose();
        dos.close();
    }

    public void server_start_temp(String dir) throws IOException {
        String directory = dir;
        ServerSocket server = new ServerSocket(21);
        File[] files = new File(directory).listFiles();
        Socket socket;
        socket = server.accept();
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(files.length);
        BufferedImage bImage = null;
        for(File file : files)
        {
            long length = file.length();
            dos.writeLong(length);

            String name = file.getName();
            dos.writeUTF(name);
            bImage = ImageIO.read(file);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            int theByte = 0;
            /*while((theByte = bis.read()) != -1) {
                //bos.write(theByte);
            }*/
            ImageIO.write(bImage,"jpg",bos);
            bis.close();

        }
        dos.close();
    }
}
