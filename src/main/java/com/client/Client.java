package com.client;

import com.ChatController;
import com.album.Client2;
import com.connection.Connection;
import com.connection.Message;
import com.connection.MessageType;
import com.files.Folder;
import com.files.Photo;
import explorer.Changes;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;


public class Client {
    private final int userNumber;
    private boolean clientConnected = false;
    private boolean clientCanConnect = true;
    private Connection connection;
    private String token;
    private long id;
    private Folder sessionFolder;
    private String pathToFolder = "E:\\Тест\\Тест\\ТестВложенный";
    private ArrayList<Photo> sendedPhotos;
    private int cnt_photos = 0;

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

    public String getPathToFolder() {
        return pathToFolder;
    }

    public void setPathToFolder(String pathToFolder) {
        this.pathToFolder = pathToFolder;
    }

    public void sendFolder(Folder folder) {
        try {
            if (this.userNumber == 1) {
                sessionFolder = folder;
                cnt_photos = sessionFolder.getFiles().size();
                connection.sendMessage(new Message(MessageType.FOLDER, new Folder(folder.getName(), folder.getId()), id, token));
            } else {
                connection.sendMessage(new Message(MessageType.FOLDER, folder, id, token));
            }
        } catch (IOException e) {
            e.printStackTrace();
            clientConnected = false;
        }
    }

    public void sendPhoto(Photo photo, int cnt) {
        try {
            connection.sendMessage(new Message(MessageType.PHOTO, photo, cnt, id, token));
        } catch (IOException e) {
            e.printStackTrace();
            clientConnected = false;
        }
    }

    public void sendMessage(Message message) {
        try {
            connection.sendMessage(message);
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

    public void sendChanges(Changes changes) {
        try {
            connection.sendMessage(new Message(MessageType.CHANGES, changes, id, token));
        } catch (IOException e) {
            e.printStackTrace();
            clientConnected = false;
        }
    }

    public void sendRequest(Folder folder) {
        try {
            connection.sendMessage(new Message(MessageType.DOWNLOAD_PHOTO, folder, id, token));
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
        void connectionStatusChange(boolean value) {
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
                System.out.println(message.getMsgType());
                if (message.getMsgType() == MessageType.PHOTO) {
                    System.out.println("Получено фото: " + message.getPhoto());
                    Client2.downloadFiles(ClientData.getPathToFolder() + "\\" + sessionFolder.getName(), message.getPhoto(), message.getCnt_photo());
                    ClientData.setCntPhotos(message.getCnt_photo());
                    if (ClientData.getCntPhotos() == cnt_photos) {
                        ClientData.setDownloadEnded(true);
                    }
                } else if (message.getMsgType() == MessageType.FOLDER) {
                    System.out.println("Получена папка!!");
                    Client2.addNewFolder(message.getFolder());
                    sessionFolder = message.getFolder();
                } else if (message.getMsgType() == MessageType.TEXT) {
                    ChatController.setText(message.getText());
                } else if (message.getMsgType() == MessageType.DOWNLOAD_PHOTO) {
                    sendMessage(new Message(MessageType.PHOTO_CNT, String.valueOf(cnt_photos), id, token));
                    ArrayList<Photo> photosToSend = com.album.Client1.getCompressedFiles(getPathToFolder() + "\\" + sessionFolder.getName());
                    sendedPhotos = photosToSend;
                    sendedPhotos.forEach(item -> System.out.println(item.toString()));
                    int i = 1;
                    for (Photo photo : photosToSend) {
                        sendPhoto(photo, i);
                        i++;
                    }
                } else if (message.getMsgType() == MessageType.PHOTO_CNT) {
                    cnt_photos = Integer.parseInt(message.getText());
                } else if (message.getMsgType() == MessageType.CHANGES) {
                    Changes.acceptChanges(message.getChanges(), getPathToFolder());
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
                connectionStatusChange(false);
                System.out.println("Ошибка в подключении!sdf");
            } catch (Exception e) {
                connectionStatusChange(false);
                System.out.println("Ошибка в подключении!\n" + e);
            }
        }
    }

}