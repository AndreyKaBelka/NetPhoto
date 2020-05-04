package com.album;

import java.math.BigInteger;
import java.util.Date;

/**
 * @authors Hukuma and Andreyka
 */

public class Main {

    public static void main(String[] args) {
        Date date = new Date();
        String time = String.valueOf(date.getTime());
        System.out.println(time);
        time = time.substring(0, time.length()-5);
        System.out.println(time);
        String IP = "192.154.028.12";
        StringBuilder bytes = new StringBuilder();
        IP.chars().forEach(bytes::append);
        System.out.println(bytes.toString());
        BigInteger encr = new BigInteger(bytes.toString()).add(new BigInteger(time));
        String en = encr.toString() + "20";
        System.out.println(en);
        String encrypted = Base64.encode(en);
        System.out.println(encrypted);
        String decrypt = new String(Base64.decode(encrypted)).trim();
        String IDFolder = decrypt.substring(decrypt.length()-1);
        decrypt = decrypt.substring(0, decrypt.length()-2);
        System.out.println(decrypt);
        String decr = new BigInteger(decrypt).subtract(new BigInteger(time)).toString();
        System.out.println(decr);
        byte[] decrBytes = new byte[decr.length()/2];
        for (int i = 0; i < decr.length()/2; i++) {
            decrBytes[i] = Byte.parseByte(decr.substring(i * 2, (i + 1) * 2));
        }
        System.out.println(new String(decrBytes));
    }
}
