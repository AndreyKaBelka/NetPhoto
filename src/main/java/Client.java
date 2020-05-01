import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {

        String dirPath = "C:/Users/Power/Downloads/";

        Socket socket = new Socket(InetAddress.getLocalHost(), 8030);
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        DataInputStream dis = new DataInputStream(bis);

        int filesCount = dis.readInt();
        File[] files = new File[filesCount];

        for(int i = 0; i < filesCount; i++)
        {
            long fileLength = dis.readLong();
            String fileName = dis.readUTF();

            files[i] = new File(dirPath + "/" + fileName);

            FileOutputStream fos = new FileOutputStream(files[i]);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            for(int j = 0; j < fileLength; j++) bos.write(bis.read());

            bos.close();
        }

        dis.close();
    }

}
