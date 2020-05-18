import com.files.Folder;
import com.files.Photo;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    private final int userNumber;
    private volatile boolean clientConnected = false;
    private Connection connection;
    private String token;
    private int id;


    public Client(int userNumber) {
        this.userNumber = userNumber;
        this.token = "sdfsdf";
        this.id = hashCode();
    }

    public static void main(String[] args) {
        Client client = new Client(1);
        client.run();
    }

    public class SocketThread extends Thread {
        protected void connectionStatusChange(boolean value){
            Client.this.clientConnected = value;
            System.out.println("Я здесь!");
            synchronized (Client.this){
                Client.this.notify();
            }
        }

        void onConnection() throws IOException, ClassNotFoundException {
            Message message;
            for (; ; ) {
                message = connection.getMessage();
                if (message.getMsgType() == MessageType.NUMBER_USER_REQUEST) {
                    connection.sendMessage(new Message((userNumber == 1) ? MessageType.MSG_USER1 : MessageType.MSG_USER2, token, id));
                } else if (message.getMsgType() == MessageType.CONNECTED){
                    connectionStatusChange(true);
                    break;
                }
            }
        }

        void clientLoop() throws Exception {
            Message message;
            for(;;){
                message = connection.getMessage();
                if (message.getMsgType() == MessageType.PHOTO){
                    System.out.println("Получена фотка!!");
                    //TODO: Обработка получения клиентом фото
                } else if(message.getMsgType() == MessageType.FOLDER){
                    System.out.println("Получена папка!!");
                    //TODO: Обработка получения клиентом папки
                } else if (message.getMsgType() == MessageType.TEXT){
                    System.out.println("Получено сообщение!!");
                    //TODO: Обработка получения клиентом сообщения
                } else {
                    connection.close();
                    throw new Exception("Ошибка!");
                }
            }
        }

        @Override
        public void run() {
            String serverIP = getServerIP();
            int  serverPort = getServerPort();
            try(Socket socket = new Socket(serverIP, serverPort)){
                connection = new Connection(socket);
                onConnection();
                clientLoop();
            } catch (IOException | ClassNotFoundException e){
                System.out.println("Ошибка в подключении!sdf");
            } catch (Exception e) {
                System.out.println("Ошибка в подключении!\n" + e);
            }
        }
    }

    private int getServerPort() {
        return 3443;//TODO: Реализовать получение порта сервера
    }

    private String getServerIP() {
        return "0.0.0.0";//TODO: Реализовать получение IP сервера
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void sendPhoto(Photo photo){
        try {
            connection.sendMessage(new Message(MessageType.PHOTO, photo, id));
        } catch (IOException e) {
            e.printStackTrace();
            clientConnected = false;
        }
    }

    public void sendFolder(Folder folder){
        try {
            connection.sendMessage(new Message(MessageType.FOLDER, folder, id));
        } catch (IOException e) {
            e.printStackTrace();
            clientConnected = false;
        }
    }

    public void sendText(String text){
        try {
            connection.sendMessage(new Message(MessageType.TEXT, text, id));
        } catch (IOException e) {
            e.printStackTrace();
            clientConnected = false;
        }
    }

    public void run() {
        SocketThread socketThread = new SocketThread();
        socketThread.setDaemon(true);
        socketThread.start();
        synchronized (this){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
        if (clientConnected){
            System.out.println("Подключение установлено!");
            String input;
            Scanner in = new Scanner(System.in);
            while (clientConnected){
                input = in.nextLine();
                sendText(input);
            }
            //TODO: Реализовать отправку сообщений!
        }
    }

}