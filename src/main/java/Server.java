import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8030);//запускаем сервер на порту 8030
        Socket socket;
        socket = server.accept();
        final File myFile= new File("D:/4game/icons"); //sdcard/DCIM.JPG
        byte[] mybytearray = new byte[8192];
        FileInputStream fis = new FileInputStream(myFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DataInputStream dis = new DataInputStream(bis);
        OutputStream os;
        try {
            //Socket socket = new Socket();
            os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(myFile.getName());
            dos.writeLong(mybytearray.length);
            int read;
            while((read = dis.read(mybytearray)) != -1){
                dos.write(mybytearray, 0, read);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
