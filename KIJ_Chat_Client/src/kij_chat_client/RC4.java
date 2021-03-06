/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;
import javax.crypto.spec.*;
import java.security.*;
import javax.crypto.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 *
 * @author Administrator
 * FROM http://stackoverflow.com/questions/12289717/rc4-encryption-java
 */
public class RC4 {
private static String algorithm = "RC4";
 
   public static String encrypt(String toEncrypt, String key) throws Exception {
      // create a binary key from the argument key (seed)
      SecureRandom sr = new SecureRandom(key.getBytes());
      KeyGenerator kg = KeyGenerator.getInstance(algorithm);
      kg.init(sr);
      SecretKey sk = kg.generateKey();
 
      // create an instance of cipher
      Cipher cipher = Cipher.getInstance(algorithm);
 
      // initialize the cipher with the key
      cipher.init(Cipher.ENCRYPT_MODE, sk);
 
      // enctypt!
      byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());
 
      String encryptedValue = new BASE64Encoder().encode(encrypted);
      return encryptedValue;
        
      //return encrypted;
   }
 
   public static String decrypt(String toDecrypt, String key) throws Exception {
      // create a binary key from the argument key (seed)
      SecureRandom sr = new SecureRandom(key.getBytes());
      KeyGenerator kg = KeyGenerator.getInstance(algorithm);
      kg.init(sr);
      SecretKey sk = kg.generateKey();
 
      // do the decryption with that key
      Cipher cipher = Cipher.getInstance(algorithm);
      cipher.init(Cipher.DECRYPT_MODE, sk);
      byte[] decordedValue = new BASE64Decoder().decodeBuffer(toDecrypt);
      byte[] decrypted = cipher.doFinal(decordedValue);
      String decryptedValue = new String(decrypted);
      return decryptedValue;
   }
}
