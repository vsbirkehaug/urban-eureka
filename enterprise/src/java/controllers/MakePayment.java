/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.Jdbc;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Claim;
import model.ClaimResponse;
import model.Payment;
import services.ClaimChecker;

/**
 *
 * @author Vilde
 */
public class MakePayment extends HttpServlet {
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
            case "submitpayment": {
               addPayment(request);   
              
                request.getRequestDispatcher("/WEB-INF/makePaymentConf.jsp").forward(request, response);
                break;
            }
            
            default: {
                //ERROR
            }
        } 
    }  
       
    private void addPayment(HttpServletRequest request) {
        
        HttpSession session = request.getSession();
        Jdbc jdbc = (Jdbc)session.getAttribute("dbbean"); 
        if(jdbc == null) {                   
            jdbc = new Jdbc();    
            session.setAttribute("dbbean", jdbc);
        }
        Connection connection = (Connection)request.getServletContext().getAttribute("connection");
        jdbc.connect(connection);
            
        int userId = ((int)request.getSession().getAttribute("id"));
        int chargeId = Integer.valueOf(request.getParameter("chargeId"));
        float amount = Float.valueOf((String)request.getParameter("amount"));
        String paymentType = (String)request.getParameter("type");

        Payment payment = new Payment(userId, chargeId, amount, paymentType);
        
        Payment responsePayment = jdbc.insertPayment(payment);

        session.setAttribute("paymentamount", String.valueOf(responsePayment.getAmount()));
        session.setAttribute("paymentid", String.valueOf(responsePayment.getId()));
        session.setAttribute("paymentmessage", "Payment added successfully");
        
    }   
}
