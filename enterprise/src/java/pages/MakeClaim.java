/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pages;

import services.ClaimChecker;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import model.Claim;
import model.ClaimResponse;
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
               addClaim(request);   
              
                request.getRequestDispatcher("/WEB-INF/makeClaimConf.jsp").forward(request, response);
                break;
            }
            
            default: {
                //ERROR
            }
        } 
    }  
       
    private void addClaim(HttpServletRequest request) {
        
        HttpSession session = request.getSession();
        Jdbc jdbc = (Jdbc)session.getAttribute("dbbean"); 
        if(jdbc == null) {                   
            jdbc = new Jdbc();    
            session.setAttribute("dbbean", jdbc);
        }
        Connection connection = (Connection)request.getServletContext().getAttribute("connection");
        jdbc.connect(connection);
        
        int userId = ((int)request.getSession().getAttribute("id"));
        float amount = Float.valueOf((String)request.getParameter("amount"));
        java.sql.Date date = java.sql.Date.valueOf((String)request.getParameter("date"));
        String rationale = (String)request.getParameter(((String)"rationale").trim());       
        Claim claim = new Claim(userId, amount, date, rationale);    
        
        ClaimChecker cc = new ClaimChecker((Jdbc)session.getAttribute("dbbean"), (int)session.getAttribute("id"), claim);
        ClaimResponse cr = cc.isValid();
        
        Claim responseClaim = null;
        if(!cr.isValid()) {
            claim.setStatus("DECLINED");       
        } 
        responseClaim = jdbc.insertClaim(claim);
        
        session.setAttribute("claimamount", String.valueOf(responseClaim.getAmount()));
        session.setAttribute("claimid", String.valueOf(responseClaim.getId()));
        session.setAttribute("claimstatus", String.valueOf(responseClaim.getStatus()));
        session.setAttribute("claimmessage", cr.getReason());
        
    }   
}
