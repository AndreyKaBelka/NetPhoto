import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        List<Socket> sockets_P1 = new ArrayList<>();//создаём лист для хранения сокетов
        List<Socket> sockets_P2 = new ArrayList<>();//создаём лист для хранения сокетов
        new Thread(() -> {//создаём поток
            try {
                ServerSocket server = new ServerSocket(21);//запускаем сервер на порту 8030
                while (true) {//бесконечный цикл для неограниченного кол-ва подключений
                    Socket client = null;//создаём сокет
                    client = server.accept();//ожидаем подключение клиента
                    BufferedReader dis = new BufferedReader(new InputStreamReader(client.getInputStream( )));
                    String p = dis.readLine();//читаем буфер
                    System.out.println(p);
                    if (p.equals("P1")) sockets_P1.add(client);//добавляем его в лист
                    else if (p.equals("P2"))  sockets_P2.add(client);//добавляем его в лист
                    else System.out.println("Неизвестный пользователь");
                }
            }catch (IOException e) {
                System.out.println(" ошибка : " + e);//выводим в случае ошибки
            }
        }).start();//запускаем поток
        while (true) {//бесконечный цикл для ввода сообщений
            for (Socket sock1 : sockets_P1) {//цикл для всех созданных сокетов
                for (Socket sock2 : sockets_P2) {//цикл для всех созданных сокетов
                    sock1.
                }
            }
        }
    }
}