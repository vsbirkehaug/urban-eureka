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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.AdminClaim;
import model.Charge;
import model.ChargeStatus;
import model.ChargeType;
import model.Claim;
import model.ClaimStatus;
import model.Member;
import model.MemberStatus;
import model.Payment;
import model.SimpleMember;

/**
 *
 * @author vilde
 */
public class PageRouter extends HttpServlet {

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

        HttpSession session = request.getSession();

        response.setContentType("text/html;charset=UTF-8");

        Jdbc dbBean = new Jdbc();
        Connection connection = (Connection) request.getServletContext().getAttribute("connection");
        dbBean.connect(connection);
        session.setAttribute("dbbean", dbBean);

        if (connection == null) {
            request.getRequestDispatcher("/WEB-INF/conErr.jsp").forward(request, response);
        }

        switch (request.getParameter("action")) {
            case "dashboard": {
                request.getRequestDispatcher("/WEB-INF/userDashboard.jsp").forward(request, response);
                break;
            }
            case "makeclaim": {
                request.getRequestDispatcher("/WEB-INF/makeClaim.jsp").forward(request, response);
                break;
            }
            case "adminmakecharge": {
                loadSimpleMembers(dbBean, request);
                attachChargeTypes(request);
                request.getRequestDispatcher("/WEB-INF/makeCharge.jsp").forward(request, response);
                break;
            }
            case "login": {
                request.getRequestDispatcher("index_user_login.jsp").forward(request, response);
                break;
            }
            case "makepayment": {
                List<Charge> charges = dbBean.getDueChargesForUser((int) session.getAttribute("id"));
                request.setAttribute("list", charges);
                request.getRequestDispatcher("/WEB-INF/makePayment.jsp").forward(request, response);
                break;
            }

            case "adminhandlecharges": {
                loadPendingCharges(dbBean, request);
                request.getRequestDispatcher("/WEB-INF/handleCharges.jsp").forward(request, response);
                break;
            }
            case "adminhandleclaims": {
                loadPendingClaims(dbBean, request);
                request.getRequestDispatcher("/WEB-INF/handleClaims.jsp").forward(request, response);
                break;
            }
            case "submitclaimchange": {
                int claimId = Integer.valueOf(request.getParameter("claimId"));
                ClaimStatus status = ClaimStatus.valueOf((String) request.getParameter("status"));

                dbBean.updateClaimStatus(claimId, status);

                int rowschanged = 1;
                request.setAttribute("rowschanged", String.valueOf(rowschanged));
                loadPendingClaims(dbBean, request);

                request.getRequestDispatcher("/WEB-INF/handleClaims.jsp").forward(request, response);
                break;

            }
            case "submitcharge": {
                int userId = Integer.valueOf(request.getParameter("member"));
                float amount = Float.valueOf(request.getParameter("amount"));
                String note = (String) request.getParameter("chargetype");

                dbBean.insertCharge(userId, amount, note);
                loadSimpleMembers(dbBean, request);
                attachChargeTypes(request);
                request.setAttribute("rowschanged", String.valueOf(1));
                request.getRequestDispatcher("/WEB-INF/makeCharge.jsp").forward(request, response);
                break;

            }
            case "submitchargechange": {
                String[] chargeIds = request.getParameterValues("chargeId[]");
                int[] chargeId = new int[chargeIds.length];
                for (int i = 0; i < chargeIds.length; i++) {
                    chargeId[i] = Integer.parseInt(chargeIds[i]);
                }

                ChargeStatus status = ChargeStatus.valueOf((String) request.getParameter("status"));
                for (int id : chargeId) {
                    dbBean.updateChargeStatus(id, status);
                }
                int rowschanged = chargeId.length;
                request.setAttribute("rowschanged", String.valueOf(rowschanged));
                loadPendingCharges(dbBean, request);
                updateUserStatus(dbBean, request, status, chargeId);

                request.getRequestDispatcher("/WEB-INF/handleCharges.jsp").forward(request, response);
                break;

            }
            case "paymenthistory": {
                List<Payment> payments = dbBean.getAllPaymentsForUser((int) session.getAttribute("id"));
                request.setAttribute("list", payments);
                request.setAttribute("listcount", payments.size());
                request.getRequestDispatcher("/WEB-INF/paymentHistory.jsp").forward(request, response);
                break;
            }
            case "claimhistory": {
                List<Claim> claims = dbBean.getAllClaimsForUser((int) session.getAttribute("id"));
                request.setAttribute("list", claims);
                request.setAttribute("listcount", claims.size());
                request.getRequestDispatcher("/WEB-INF/claimHistory.jsp").forward(request, response);
                break;
            }
            case "adminclaimhistory": {
                List<Claim> claims = dbBean.getAllClaims();
                request.setAttribute("list", claims);
                request.setAttribute("listcount", claims.size());
                request.getRequestDispatcher("/WEB-INF/adminClaimHistory.jsp").forward(request, response);
                break;
            }

            case "admindashboard": {
                request.getRequestDispatcher("/WEB-INF/adminDashboard.jsp").forward(request, response);
            }
            default: {
                request.getRequestDispatcher("/WEB-INF/conErr.jsp").forward(request, response);
                break;
            }

        }
    }

    private void loadSimpleMembers(Jdbc dbBean, HttpServletRequest request) {
        List<SimpleMember> members = dbBean.getMembers();
        request.setAttribute("list", members);
        if (members != null) {
            request.setAttribute("listcount", members.size());
        } else {
            request.setAttribute("listcount", 0);
        }
    }

    private void attachChargeTypes(HttpServletRequest request) {
        ChargeType[] ct = ChargeType.values();
        String[] cts = new String[ct.length];
        for (int i = 0; i < cts.length; i++) {
            cts[i] = ct[i].toString();
        }
        request.setAttribute("chargelist", cts);
    }

    private void loadPendingCharges(Jdbc dbBean, HttpServletRequest request) {
        List<Charge> charges = dbBean.getAllChargesWhereStatus(ChargeStatus.PENDING);
        request.setAttribute("list", charges);
        if (charges != null) {
            request.setAttribute("listcount", charges.size());
        } else {
            request.setAttribute("listcount", 0);
        }
    }

    private void loadPendingClaims(Jdbc dbBean, HttpServletRequest request) {
        List<AdminClaim> claims = dbBean.getAllClaimsWhereStatus(ClaimStatus.PENDING);
        request.setAttribute("list", claims);
        if (claims != null) {
            request.setAttribute("listcount", String.valueOf(claims.size()));
        } else {
            request.setAttribute("listcount", String.valueOf(0));
        }
    }

    private void updateUserStatus(Jdbc dbBean, HttpServletRequest request, ChargeStatus status, int[] chargeIds) {

        for (int id : chargeIds) {
            Charge charge = dbBean.getCharge(id);
            int unapprovedCharges = dbBean.getDueOrDeclinedChargeCount(charge.getUserId());

            if (status.equals(ChargeStatus.APPROVED) && unapprovedCharges == 0) {
                dbBean.updateMemberStatus(charge.getUserId(), MemberStatus.ACTIVE);
            } else if (status.equals(ChargeStatus.DECLINED)) {
                dbBean.updateMemberStatus(charge.getUserId(), MemberStatus.SUSPENDED);
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
