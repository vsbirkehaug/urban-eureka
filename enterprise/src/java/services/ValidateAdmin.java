/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;
import java.sql.*;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vilde
 */
public class ValidateAdmin {
      public static int getAdmin(String username, String password, Connection conn) 
     {
      int userId = 0;
      try{

	 //loading drivers for mysql
         Class.forName("com.mysql.jdbc.Driver");

         PreparedStatement ps = conn.prepareStatement("SELECT * FROM admins WHERE username = ? AND password = ?"); 
         ps.setString(1, username);
         ps.setString(2, password);
         ResultSet rs = ps.executeQuery(); 
         
         if(rs.next()) {
            userId = rs.getInt("id");
         } else {
            return -1;
         }

      }
      catch(Exception e)
      {
          e.printStackTrace();
      }           
      
      return userId;
  }   
}
