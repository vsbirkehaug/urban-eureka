/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import java.util.Enumeration;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.ServletContext;

/**
 * Web application lifecycle listener.
 *
 * @author me-aydin
 */
@WebListener()
public class UserServletListener implements ServletContextListener {

    private Connection conn = null;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        String db = sc.getInitParameter("dbname");
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db.trim(), "root", "root");
        }
        catch(ClassNotFoundException e){
            sc.setAttribute("error", e.toString());
        } catch (SQLException e) {
            sc.setAttribute("error", e.toString());
        } catch (Exception e) {
            sc.setAttribute("error", e.toString());
        }
        
        //testing
//        if(conn == null) {
//             sc.setAttribute("error", "No idea");
//        } else {
//             sc.setAttribute("error", "Conn is null");
//        }
        
        sc.setAttribute("connection", conn);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        try { conn.close(); } catch(SQLException e) {}
    }
}
