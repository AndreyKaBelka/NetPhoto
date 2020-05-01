import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8030);//запускаем сервер на порту 8030
        Socket socket = null;
        socket = server.accept();
        final File myFile= new File("D:/4game/icons"); //sdcard/DCIM.JPG
        File folder = new File("D:/4game/language-files");
        File[] listOfFiles = folder.listFiles();
        byte[] mybytearray = new byte[8192];
        //FileInputStream fis = new FileInputStream(listOfFiles);
        //BufferedInputStream bis = new BufferedInputStream(fis);
        //DataInputStream dis = new DataInputStream(bis);
        OutputStream os;
        try {
            //Socket socket = new Socket();
            os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            for (int i = 0; i < listOfFiles.length; i++) {
                FileInputStream fis = new FileInputStream(listOfFiles[i]);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                dos.writeUTF(listOfFiles[i].getName( ));
                System.out.println(listOfFiles[i].getName( ));
                dos.writeLong(mybytearray.length);
                int read;
                while ((read = dis.read(mybytearray)) != -1) {
                    dos.write(mybytearray, 0, read);
                }
                dos.close();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
