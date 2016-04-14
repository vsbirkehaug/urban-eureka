/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;
import java.sql.*;

/**
 *
 * @author Vilde
 */
public class ValidateUser {
      public static String[] getUser(String username, String password, Connection conn) 
     {
      String name = null;
      int userId = 0;
      try{

	 //loading drivers for mysql
         Class.forName("com.mysql.jdbc.Driver");

         PreparedStatement ps = conn.prepareStatement("SELECT * FROM members WHERE username = ? AND password = ?"); 
         ps.setString(1, username);
         ps.setString(2, password);
         ResultSet rs = ps.executeQuery(); 
         
         if(rs.next()) {
            name = rs.getString("name");
            userId = rs.getInt("id");
         } else {
            return null;
         }

      }
      catch(Exception e)
      {
          e.printStackTrace();
      }           
      
      return new String[]{String.valueOf(userId), name};
  }   
}
