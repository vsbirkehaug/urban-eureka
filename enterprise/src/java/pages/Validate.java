/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pages;
import java.sql.*;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vilde
 */
public class Validate {
      public static boolean checkUser(String username, String password, Connection conn) 
     {
      boolean st =false;
      try{

	 //loading drivers for mysql
         Class.forName("com.mysql.jdbc.Driver");

         PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ? and password = ?");
         ps.setString(1, username);
         ps.setString(2, password);
         ResultSet rs = ps.executeQuery(); 
         st = rs.next();

      }
      catch(Exception e)
      {
          e.printStackTrace();
      }
      return st;                 
  }   
}
