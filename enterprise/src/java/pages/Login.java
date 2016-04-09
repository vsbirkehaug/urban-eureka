/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pages;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import model.Jdbc;

/**
 *
 * @author Vilde
 */
public class Login extends HttpServlet {
       protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();
        Jdbc jdbc = (Jdbc)session.getAttribute("dbbean"); 
        if(jdbc == null) {                   
            jdbc = new Jdbc();    
            session.setAttribute("dbbean", jdbc);
        }
        Connection connection = (Connection)request.getServletContext().getAttribute("connection");
        jdbc.connect(connection);
          
        PrintWriter out = response.getWriter();
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");    
        
        if(Validate.checkUser(username, password, connection)) 
        {            
            session.setAttribute("username", username);
            request.setAttribute("message", "Welcome, " + username + ".");    
            request.getRequestDispatcher("/WEB-INF/userDashboard.jsp").forward(request, response);
        }
        else
        {
            request.setAttribute("message", "Incorrect username or password.");     
            request.getRequestDispatcher("/index_user_login.jsp").forward(request, response);
        }
    }  
}
