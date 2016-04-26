/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

/**
 *
 * @author USER
 */
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import sun.misc.*;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;


public class AES{
    
     //private static final String ALGO = "AES";
     //static String IV = "AAAAAAAAAAAAAAAA";

public static String encrypt(String Data, String keys, String IV) throws Exception {
        Key key = generateKey(keys);
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING", "SunJCE");
        c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
        byte[] encVal = c.doFinal(Data.getBytes("UTF-8"));
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }

    public static String decrypt(String encryptedData, String keys, String IV) throws Exception {
        Key key = generateKey(keys);
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING", "SunJCE");
        c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
    private static Key generateKey(String keyValue) throws Exception {
        Key key = new SecretKeySpec(keyValue.getBytes("UTF-8"), "AES");
        return key;
}

}