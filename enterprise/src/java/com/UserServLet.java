/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.IOException;
//import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Jdbc;

/**
 *
 * @author me-aydin
 */
public class UserServLet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String qry = "select * from users";
       
        HttpSession session = request.getSession();
        
        response.setContentType("text/html;charset=UTF-8");
        
        //testing, remove before sub
        request.setAttribute("error", request.getServletContext().getAttribute("error"));
        
        Jdbc dbBean = new Jdbc();
        Connection connection = (Connection)request.getServletContext().getAttribute("connection");
        dbBean.connect(connection);
        session.setAttribute("dbbean", dbBean);
       
        if(connection == null)
            request.getRequestDispatcher("/WEB-INF/conErr.jsp").forward(request, response);
       
        
        switch(request.getParameter("tbl")) {
            case "List": {
                 String msg="No users";
                try {
                    msg = dbBean.retrieve(qry);
                } catch (SQLException ex) {
                    Logger.getLogger(UserServLet.class.getName()).log(Level.SEVERE, null, ex);
                }
                request.setAttribute("query", msg);
                request.getRequestDispatcher("/WEB-INF/results.jsp").forward(request, response);
                break;
            }
            case "NewUser": {
                request.getRequestDispatcher("/WEB-INF/user.jsp").forward(request, response);
                break;
            }
            case "Update": {
                  request.getRequestDispatcher("/WEB-INF/passwdChange.jsp").forward(request, response);    
                  break;
            }
            default: {
                  request.setAttribute("msg", "del");
            request.getRequestDispatcher("/WEB-INF/user.jsp").forward(request, response); 
            }
            
        }
        
    }
      

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
