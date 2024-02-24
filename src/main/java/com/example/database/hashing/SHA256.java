package com.example.database.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    public static byte[] hash(String key){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA256");
            byte[] hashBytes = digest.digest(key.getBytes());
//            printByteArrayAsBits(hashBytes);
            return hashBytes;
        } catch (NoSuchAlgorithmException e){
            System.err.println("SHA-256 algorithm not found.");
        }
        return null;
    }

    public static void printByteArrayAsBits(byte[] byteArray) {
        for (byte b : byteArray) {
            // Print each byte as bits
            for (int i = 7; i >= 0; i--) {
                System.out.print((b >> i) & 1);
            }
            System.out.print(" ");
        }
        System.out.println();
    }

}
