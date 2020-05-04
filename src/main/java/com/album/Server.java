package com.album;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public void server_start(String dir) throws IOException {
        String directory = dir;
        ServerSocket server = new ServerSocket(8030);
        File[] files = new File(directory).listFiles();
        Socket socket;
        socket = server.accept();
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(files.length);
        for(File file : files)
        {
            long length = file.length();
            dos.writeLong(length);

            String name = file.getName();
            dos.writeUTF(name);

            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            int theByte = 0;
            while((theByte = bis.read()) != -1) bos.write(theByte);

            bis.close();

        }
        dos.close();
    }
}