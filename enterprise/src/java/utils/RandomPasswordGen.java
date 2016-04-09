/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 *
 * @author Vilde
 * Copied from http://www.java2s.com/Code/Java/Security/GeneratearandomStringsuitableforuseasatemporarypassword.htm [Accessed 09.04.2016]
 */
public class RandomPasswordGen {
    
    private static final Random RANDOM = new SecureRandom();
  /** Length of password. @see #generateRandomPassword() */
  public static final int PASSWORD_LENGTH = 8;
  /**
   * Generate a random String suitable for use as a temporary password.
   *
   * @return String suitable for use as a temporary password
   * @since 2.4
   */
  public static String generateRandomPassword()
  {
      // Pick from some letters that won't be easily mistaken for each
      // other. So, for example, omit o O and 0, 1 l and L.
      String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+@";

      String pw = "";
      for (int i=0; i<PASSWORD_LENGTH; i++)
      {
          int index = (int)(RANDOM.nextDouble()*letters.length());
          pw += letters.substring(index, index+1);
      }
      return pw;
  }
    
}
