import javafx.scene.layout.Pane;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class Encrypt {

    public static String encrypt(String folderid) {
        Date date = new Date();
        String time = String.valueOf(date.getTime());
        System.out.println(time);
        time = time.substring(0, time.length()-5);
        System.out.println(time);
        String IP = null;
        try {
            IP = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            new Pane();
        }
        StringBuilder bytes = new StringBuilder();
        IP.chars().forEach(bytes::append);
        System.out.println(bytes.toString());
        BigInteger encr = new BigInteger(bytes.toString()).add(new BigInteger(time));
        String en = encr.toString() + folderid;
        System.out.println(en);
        String encrypted = Base64.encode(en);
        System.out.println(encrypted);
        return encrypted;
    }
}
