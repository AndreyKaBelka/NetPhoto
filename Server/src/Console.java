import java.util.Date;

class Console {
    static void writeMessage(String text) {
        System.out.println("<" + new Date() + "> " + text);
    }
}
