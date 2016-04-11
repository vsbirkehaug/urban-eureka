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
public class MakeClaim extends HttpServlet {
       protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
          
        PrintWriter out = response.getWriter();
        
        String action = request.getParameter("action");
        
        switch(action) {
            case "cancel": {
                request.getRequestDispatcher("/WEB-INF/userDashboard.jsp").forward(request, response);
                break;
            }
            case "submitclaim": {
                String[] userResponse = addClaim(request);                 
                request.getRequestDispatcher("/WEB-INF/makeClaimConf.jsp").forward(request, response);
                break;
            }
            
            default: {
                //ERROR
            }
        } 
    }  
       
    private String[] addClaim(HttpServletRequest request) {
        
        HttpSession session = request.getSession();
        Jdbc jdbc = (Jdbc)session.getAttribute("dbbean"); 
        if(jdbc == null) {                   
            jdbc = new Jdbc();    
            session.setAttribute("dbbean", jdbc);
        }
        Connection connection = (Connection)request.getServletContext().getAttribute("connection");
        jdbc.connect(connection);
        
        String member_id = ((String)request.getSession().getAttribute("username")).trim();
        float amount = Float.valueOf((String)request.getParameter("amount"));
        java.sql.Date date = java.sql.Date.valueOf((String)request.getParameter("date"));
        String rationale = (String)request.getParameter(((String)"rationale").trim());
        
        Object[] queryStrings = new Object[]{member_id, amount, date, rationale};
              
        return jdbc.insertClaim(queryStrings);
        
    }   
}
