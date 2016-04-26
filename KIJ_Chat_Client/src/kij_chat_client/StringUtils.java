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
public class StringUtils {
  private StringUtils() {}

  // pad with " " to the right to the given length (n)
  public static String padRight(String s, int n) {
    return String.format("%1$-" + n + "s", s);
  }

  // pad with " " to the left to the given length (n)
  public static String padLeft(String s, int n) {
    return String.format("%1$" + n + "s", s);
  }
  
}
