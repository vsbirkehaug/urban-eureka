/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import services.ValidateUser;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import com.Jdbc;
import services.ValidateAdmin;

/**
 *
 * @author Vilde
 */
public class Login extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        Jdbc jdbc = (Jdbc) session.getAttribute("dbbean");
        if (jdbc == null) {
            jdbc = new Jdbc();
            session.setAttribute("dbbean", jdbc);
        }
        Connection connection = (Connection) request.getServletContext().getAttribute("connection");
        jdbc.connect(connection);

        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String admin = "no";
        try {
            admin = request.getParameter("admin");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {

            int id = -1;
            String name = "";

            if (admin != null && admin.equals("yes")) {
                id = ValidateAdmin.getAdmin(username, password, connection);
            } else {
                String[] details = ValidateUser.getUser(username, password, connection);
                name = null;
                if (details != null) {
                    id = Integer.valueOf(details[0]);
                    name = details[1];
                }
            }

            if (id >= 0 && admin != null && admin.equals("yes")) {
                session.setAttribute("username", username);
                session.setAttribute("id", id);
                session.setAttribute("role", "admin");
                request.getRequestDispatcher("/WEB-INF/adminDashboard.jsp").forward(request, response);
            } else if (id >= 0) {
                session.setAttribute("username", username);
                session.setAttribute("name", name);
                session.setAttribute("id", id);
                session.setAttribute("role", "member");
                request.getRequestDispatcher("/WEB-INF/userDashboard.jsp").forward(request, response);
            } else {
                request.setAttribute("message", "Incorrect username or password.");
                request.getRequestDispatcher("/index_user_login.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
