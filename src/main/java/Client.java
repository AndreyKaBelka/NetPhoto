import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws FileNotFoundException {
        int bytesRead;
        InputStream in;
        int bufferSize;

        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), 8030);
            bufferSize=socket.getReceiveBufferSize();
            in=socket.getInputStream();
            DataInputStream clientData = new DataInputStream(in);
            String fileName = clientData.readUTF();
            System.out.println(fileName);
            OutputStream output = new FileOutputStream("C:/Users/Power/Downloads/"+ fileName);
            byte[] buffer = new byte[bufferSize];
            int read;
            while((read = clientData.read(buffer)) != -1){
                output.write(buffer, 0, read);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
