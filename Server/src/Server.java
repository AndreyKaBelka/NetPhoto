import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static Map<Integer, Connection> connectionArray = new ConcurrentHashMap<>();
    private static Map<String, Integer> tokens = new HashMap<>();
    private static Map<Integer, Integer> pairs = new ConcurrentHashMap<>();
    private static final int PORT = 3443;
    private static String IP = null;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            Console.writeMessage("Сервер запущен...");
            for (; ; ) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getIP() {
        return IP;
    }

    private static class ClientHandler extends Thread {
        private Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        private int onConnection(Connection connection) throws Exception {
            for (; ; ) {
                connection.sendMessage(new Message(MessageType.NUMBER_USER_REQUEST));
                Message message = connection.getMessage();
                if (message.getMsgType() == MessageType.MSG_USER1) {
                    String token = message.getText();
                    pairs.put(message.getUserId(), 0);
                    if (token != null && !tokens.containsKey(token) && !token.isEmpty()) {
                        tokens.put(token, message.getUserId());
                        try {
                            connectionArray.put(message.getUserId(), connection);
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
                        pairs.put(tokens.get(token), message.getUserId());
                        tokens.put(token, message.getUserId());
                        return message.getUserId();
                    } else {
                        connection.close();
                        throw new Exception("Неверный ключ сессии!");
                    }
                }
            }
        }

        private void serverLoopForUsers(Connection connection, int userId) throws IOException, ClassNotFoundException {
            Message message;
            for (; ; ) {
                message = connection.getMessage();
                if (message.getMsgType() == MessageType.TEXT) {
                    Console.writeMessage("Пользователь прислал сообщение!!!!");
                    //TODO: Обработка получения сообщения от пользователя
                } else if (message.getMsgType() == MessageType.PHOTO) {
                    Console.writeMessage("Пользователь прислал фото!!!");
                    //TODO: Обработка получения фото
                } else if (message.getMsgType() == MessageType.FOLDER) {
                    Console.writeMessage("Пользователь прислал информацию о папке");
                    //TODO: Обработка получения информации о папке
                } else {
                    Console.writeMessage("Ошибка отправки сообщения!!!");
                }
            }
        }

        @Override
        public void run() {
            try (Connection connection = new Connection(socket)) {
                int userID = onConnection(connection);
                serverLoopForUsers(connection, userID);
            } catch (Exception e) {
                Console.writeMessage(e.toString());
            }
        }
    }

    private static void sendToSecondUser(Message message, int secondUserId) {
        try {
            connectionArray.get(secondUserId).sendMessage(message);
        } catch (IOException e) {
            Console.writeMessage("Ошибка в отправке сообщения!");
        }

    }

}
