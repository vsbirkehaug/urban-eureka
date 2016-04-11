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
import pages.NewUser;

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
    
    
    private String getUsernameFromName(String name) {
        String[] names = name.split(" ");
        String username = "";
        
        for(int i = 0; i < names.length; i++) {
            if(i == names.length-1) {
                username +=  "-" + names[i];
            } else {
                username += names[i].substring(0,1);
            }        
        }
        
        int existingUsers = usernameExists(username);       
        while(exists(username + String.valueOf(existingUsers))) {
            existingUsers++;
        }
        if(existingUsers > 0) {
            username = username + String.valueOf(existingUsers); 
        }
        
        return username;
    }
    
    private String getPasswordFromDob(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date parsed = format.parse(date);
            SimpleDateFormat pwformat = new SimpleDateFormat("ddMMyy");
            
            return pwformat.format(parsed);
            
        } catch (ParseException ex) {
            Logger.getLogger(NewUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
       
    public String[] insertUser(String[] str){
        PreparedStatement ps = null;
        String username = getUsernameFromName(str[0].trim().toLowerCase());
        String name = str[0].trim();
        String password = getPasswordFromDob(str[2]);    
        String address = str[1].trim();
        java.sql.Date dob = java.sql.Date.valueOf(str[2]);
        java.sql.Date dor = java.sql.Date.valueOf(str[3]);
        
        int result = 0;
        try {
            ps = connection.prepareStatement("INSERT INTO members (`username`, `name`, `password`, `address`, `dob`, `dor`) VALUES (?,?,?,?,?, ?)", Statement.SUCCESS_NO_INFO);
            ps.setString(1, username);    
            ps.setString(2, name); 
            ps.setString(3, password);
            ps.setString(4, address);
            ps.setDate(5, dob); 
            ps.setDate(6, dor); 
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
        return new String[]{username, password};
    }
    
    public Claim insertClaim(Claim claim){
        PreparedStatement ps = null;
        
        String member_id = claim.getUsername();
        float amount = claim.getAmount();
        java.sql.Date date = claim.getDate();
        String rationale = claim.getRationale();
        Claim newClaim = null;

        try {
            ps = connection.prepareStatement("INSERT INTO claims (`mem_id`, `amount`, `date`, `rationale`) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, member_id);    
            ps.setFloat(2, amount); 
            ps.setDate(3, date);
            ps.setString(4, rationale);
            ps.executeUpdate();  
            ResultSet key = ps.getGeneratedKeys();    
            key.next();  
            int result = key.getInt(1);
            ps.close();
            
            ps = connection.prepareStatement("SELECT * FROM claims WHERE id = ?");
            ps.setInt(1, result);
            ResultSet rs = ps.executeQuery();
            rs.next();
            newClaim = new Claim(rs.getInt("id"), rs.getString("mem_id"), rs.getFloat("amount"), rs.getDate("date"), rs.getString("rationale"), rs.getString("status"));         
            
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        return newClaim;
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
