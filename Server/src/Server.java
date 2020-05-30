import com.connection.Connection;
import com.connection.Message;
import com.connection.MessageType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static Map<Long, Connection> connectionArray = new ConcurrentHashMap<>();
    private static Map<String, ArrayList<Long>> tokens = new HashMap<>();
    private static final int PORT = 3443;
    private static String IP = null;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            IP = serverSocket.getInetAddress().getHostAddress();
            Console.writeMessage("Сервер запущен...");
            Console.writeMessage(getIP());
            for (; ; ) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getIP() {
        return IP;
    }

    private static void sendToSecondUser(Message message) throws IOException {
        ArrayList<Long> ids = tokens.get(message.getTokenOfSession());
        long secondUserId = (ids.get(0) == message.getUserId()) ? ids.get(1) : ids.get(0);
        try {
            connectionArray.get(secondUserId).sendMessage(message);
        } catch (IOException e) {
            Console.writeMessage("Ошибка в отправке сообщения!");
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        private long onConnection(Connection connection) throws Exception {
            for (; ; ) {
                connection.sendMessage(new Message(MessageType.NUMBER_USER_REQUEST));
                Message message = connection.getMessage();
                if (message.getMsgType() == MessageType.MSG_USER1) {
                    String token = message.getText();
                    if (token != null && !token.isEmpty()) {
                        tokens.put(token, new ArrayList<>(Collections.singletonList(message.getUserId())));
                        try {
                            connectionArray.put(message.getUserId(), connection);
                            connection.sendMessage(new Message(MessageType.CONNECTED));
                            return message.getUserId();
                        } catch (IOException e) {
                            Console.writeMessage(e.toString());
                            connection.close();
                        }
                    } else {
                        connection.close();
                        throw new Exception("Неверный ключ сессии!");
                    }
                } else if (message.getMsgType() == MessageType.MSG_USER2) {
                    String token = message.getText();
                    if (token != null && !token.isEmpty() && tokens.containsKey(token)) {
                        tokens.get(token).add(message.getUserId());
                        connectionArray.put(message.getUserId(), connection);
                        connection.sendMessage(new Message(MessageType.CONNECTED));
                        return message.getUserId();
                    } else {
                        connection.close();
                        throw new Exception("Неверный ключ сессии!");
                    }
                }
            }
        }

        private void serverLoopForUsers(Connection connection, long userId) throws IOException, ClassNotFoundException {
            Message message;
            for (; ; ) {
                message = connection.getMessage();
                if (message.getMsgType() == MessageType.TEXT) {
                    Console.writeMessage("Пользователь: " + userId + " прислал сообщение: \"" + message.getText() + "\"");
                    sendToSecondUser(message);
                } else if (message.getMsgType() == MessageType.PHOTO) {
                    Console.writeMessage("Пользователь: " + userId + " прислал фотографию: " + message.getPhoto().toString());
                    sendToSecondUser(message);
                } else if (message.getMsgType() == MessageType.FOLDER) {
                    Console.writeMessage("Пользователь: " + userId + " прислал информацию о папке: " + message.getFolder().toString());
                    sendToSecondUser(message);
                } else {
                    Console.writeMessage("Ошибка отправки сообщения!!!");
                }
            }
        }

        @Override
        public void run() {
            long userID = 0;
            try (Connection connection = new Connection(socket)) {
                Console.writeMessage("Новый пользователь: " + connection.getRemoteSocketAddress().toString().replaceAll("/", ""));
                userID = onConnection(connection);
                serverLoopForUsers(connection, userID);
            } catch (Exception e) {
                Console.writeMessage(e.toString());
            } finally {
                if (userID != 0) {
                    connectionArray.remove(userID);
                }
            }
        }
    }

}
