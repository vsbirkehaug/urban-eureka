/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import com.Jdbc;

/**
 *
 * @author Vilde
 */
public class Logout extends HttpServlet {
       protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        request.getSession().invalidate();
        request.setAttribute("message", "You were successfully logged out.");     
        response.sendRedirect(request.getContextPath() + "/index_user_login.jsp");
    }  
}
