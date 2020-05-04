package com.album;

import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;

public class Crypt {

    public static String encrypt(String folderid) {
        Date date = new Date();
        String time = String.valueOf(date.getTime());
        System.out.println(time);
        time = time.substring(0, 7);
        System.out.println(time);
        String IP = null;
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            IP = in.readLine(); //you get the IP as a String
            System.out.println(IP);
        } catch (IOException e) {
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

    public static String decrypt(String key) {
        Date date = new Date();
        String time = String.valueOf(date.getTime());
        String decrypt = new String(Base64.decode(key)).trim();
        String IDFolder = decrypt.substring(decrypt.length()-2);
        decrypt = decrypt.substring(0, decrypt.length()-2);
        System.out.println(decrypt);
        String decr = new BigInteger(decrypt).subtract(new BigInteger(time.substring(0,7))).toString();
        System.out.println(decr);
        byte[] decrBytes = new byte[decr.length()/2];
        for (int i = 0; i < decr.length()/2; i++) {
            decrBytes[i] = Byte.parseByte(decr.substring(i * 2, (i + 1) * 2));
        }
        System.out.println(new String(decrBytes));
        return new String(decrBytes);
    }

    public static void main(String[] args) {
        String a = encrypt("01");
        System.out.println("aaa="+a);
        String b = decrypt(a);
        System.out.println(b.substring(8));
    }
}
