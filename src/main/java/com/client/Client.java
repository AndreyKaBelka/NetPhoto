package com.client;

import com.album.Client2;
import com.connection.Connection;
import com.connection.Message;
import com.connection.MessageType;
import com.files.Folder;
import com.files.Photo;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;


public class Client {
    private final int userNumber;
    private boolean clientConnected = false;
    private boolean clientCanConnect = true;
    private Connection connection;
    private String token;
    private long id;

    public Client(int userNumber, String token) {
        this.userNumber = userNumber;
        this.token = token;
        this.id = getId();
    }

    private long getId() {
        return new Date().getTime();
    }

    private int getUserNumberFromCMD() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите 1 - если хотите передать и 2 - если получить");
        return in.nextInt();
    }

    private String getTokenFromCMD() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите токен");
        return in.nextLine();
    }

    public void sendPhoto(Photo photo) {
        try {
            connection.sendMessage(new Message(MessageType.PHOTO, photo, id, token));
        } catch (IOException e) {
            e.printStackTrace();
            clientConnected = false;
        }
    }

    public synchronized boolean isClientCanConnect() {
        return clientCanConnect;
    }

    public synchronized boolean isClientConnected() {
        return clientConnected;
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

    public void sendFolder(Folder folder) {
        try {
            connection.sendMessage(new Message(MessageType.FOLDER, folder, id, token));
        } catch (IOException e) {
            e.printStackTrace();
            clientConnected = false;
        }
    }

    public void sendText(String text) {
        try {
            connection.sendMessage(new Message(MessageType.TEXT, text, id, token));
        } catch (IOException e) {
            e.printStackTrace();
            clientConnected = false;
        }
    }

    public void run() {
        SocketThread socketThread = new SocketThread();
        socketThread.setDaemon(true);
        socketThread.start();
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
        if (clientConnected) {
            System.out.println("Подключение установлено!");
            clientConnected = true;
            while (clientConnected) {
            }
        } else {
            System.out.println("Ошибка подключения!");
            clientCanConnect = false;
        }
    }

    public class SocketThread extends Thread {
        protected void connectionStatusChange(boolean value) {
            Client.this.clientConnected = value;
            synchronized (Client.this) {
                Client.this.notify();
            }
        }

        void onConnection() throws IOException, ClassNotFoundException {
            Message message;
            for (; ; ) {
                message = connection.getMessage();
                if (message.getMsgType() == MessageType.NUMBER_USER_REQUEST) {
                    connection.sendMessage(new Message((userNumber == 1) ? MessageType.MSG_USER1 : MessageType.MSG_USER2, token, id, token));
                } else if (message.getMsgType() == MessageType.CONNECTED) {
                    connectionStatusChange(true);
                    break;
                } else if (message.getMsgType() == MessageType.ERROR) {
                    connectionStatusChange(false);
                    throw new IOException("Неверный ключ сесии!");
                }
            }
        }

        void clientLoop() throws Exception {
            Message message;
            for (; ; ) {
                message = connection.getMessage();
                if (message.getMsgType() == MessageType.PHOTO) {
                    System.out.println("Получено фото: " + message.getPhoto());
                    Client2.downloadFiles("E:\\Полученное фото", message.getPhoto());
                } else if (message.getMsgType() == MessageType.FOLDER) {
                    System.out.println("Получена папка!!");
                    Client2.addNewFolder(message.getFolder());
                } else if (message.getMsgType() == MessageType.TEXT) {
                    System.out.println(message.getText());
                } else {
                    connection.close();
                    throw new Exception("Ошибка!");
                }
            }
        }

        @Override
        public void run() {
            String serverIP = getServerIP();
            int serverPort = getServerPort();
            try (Socket socket = new Socket(serverIP, serverPort)) {
                connection = new Connection(socket);
                onConnection();
                clientLoop();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Ошибка в подключении!sdf");
                connectionStatusChange(false);
            } catch (Exception e) {
                connectionStatusChange(false);
                System.out.println("Ошибка в подключении!\n" + e);
            }
        }
    }

}