/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import static java.sql.Types.NULL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author me-aydin
 */
public class Jdbc {
    
    Connection connection = null;
    Statement statement = null;
    ResultSet rs = null;
    //String query = null;
    
    
    public Jdbc(String query){
        //this.query = query;
    }

    public Jdbc() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void connect(Connection con){
       connection = con;
    }
    
    private ArrayList rsToList() throws SQLException {
        ArrayList aList = new ArrayList();

        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) { 
          String[] s = new String[cols];
          for (int i = 1; i <= cols; i++) {
            s[i-1] = rs.getString(i);
          } 
          aList.add(s);
        } // while    
        return aList;
    } //rsToList
 
    private String makeTable(ArrayList list) {
        StringBuilder b = new StringBuilder();
        String[] row;
        b.append("<table border=\"3\">");
        for (Object s : list) {
          b.append("<tr>");
          row = (String[]) s;
            for (String row1 : row) {
                b.append("<td>");
                b.append(row1);
                b.append("</td>");
            }
          b.append("</tr>\n");
        } // for
        b.append("</table>");
        return b.toString();
    }//makeHtmlTable
    
    private void select(String query){
        //Statement statement = null;
        
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            //statement.close();
        }
        catch(SQLException e) {
            System.out.println("way way"+e);
            //results = e.toString();
        }
    }
    public String retrieve(String query) throws SQLException {
        String results="";
        select(query);
//        try {
//            
//            if (rs==null)
//                System.out.println("rs is null");
//            else
//                results = makeTable(rsToList());
//        } catch (SQLException ex) {
//            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return makeTable(rsToList());//results;
    }
    
    public boolean exists(String user) {
        boolean exists = false;
        try  {
            PreparedStatement ps = connection.prepareStatement("SELECT (`username`) FROM members WHERE `username` = ? ");
            ps.setString(1, user);  
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {  
                exists = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exists;
    }
    
    public int usernameExists(String user) {
        int counter = 0;
        try  {
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(`username`) FROM members WHERE `username` LIKE ?");
            ps.setString(1, user + '%');  
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {  
                counter = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return counter;
    }
       
    public String insertUser(String[] str){
        PreparedStatement ps = null;
        String concatname = str[0].replace(" ", "").trim().toLowerCase();
        int existingUsers = usernameExists(concatname);       
        while(exists(concatname + String.valueOf(existingUsers))) {
            existingUsers++;
        }
        if(existingUsers > 0) {
            concatname = concatname + String.valueOf(existingUsers); 
        }
      
        int result = 0;
        try {
            ps = connection.prepareStatement("INSERT INTO members (`username`, `name`, `password`, `address`, `dob`) VALUES (?,?,?,?,?)", Statement.SUCCESS_NO_INFO);
            ps.setString(1, concatname);    
            ps.setString(2, str[0].trim()); 
            ps.setString(3, str[1].trim());
            ps.setString(4, str[2].trim());
            ps.setDate(5, java.sql.Date.valueOf(str[3])); 
            result = ps.executeUpdate();          
            ps.close();
            
            if (result == 1) {
                 System.out.println("1 row added.");
            } else {
                return null;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        return concatname;
    }
    public void update(String[] str) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("Update members Set password=? where username=?",PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, str[1].trim()); 
            ps.setString(2, str[0].trim());
            ps.executeUpdate();
        
            ps.close();
            System.out.println("1 rows updated.");
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void delete(String user){
       
      String query = "DELETE FROM members " +
                   "WHERE username = '"+user.trim()+"'";
      
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        }
        catch(SQLException e) {
            System.out.println("way way"+e);
            //results = e.toString();
        }
    }
    public void closeAll(){
        try {
            rs.close();
            statement.close(); 		
            //connection.close();                                         
        }
        catch(SQLException e) {
            System.out.println(e);
        }
    }
      
}
