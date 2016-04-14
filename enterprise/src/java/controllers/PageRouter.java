/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
//import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.AdminClaim;
import model.Charge;
import model.enums.ChargeStatus;
import model.enums.ChargeType;
import model.Claim;
import model.ClaimResponse;
import model.enums.ClaimStatus;
import model.enums.MemberStatus;
import model.Payment;
import model.BaseMember;
import model.enums.PaymentType;
import services.ClaimChecker;
import services.ValidateAdmin;
import services.ValidateUser;

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
        Jdbc dbBean = null;
        try {
            dbBean = (Jdbc) session.getAttribute("dbbean");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (dbBean == null) {
            dbBean = new Jdbc();
        }
        Connection connection = (Connection) request.getServletContext().getAttribute("connection");
        dbBean.connect(connection);
        session.setAttribute("dbbean", dbBean);

        if (connection == null) {
            request.getRequestDispatcher("/WEB-INF/conErr.jsp").forward(request, response);
        }

        switch (request.getParameter("action").toLowerCase()) {
            case "login": {
                login(request, response, connection);
                request.getRequestDispatcher("index.jsp").forward(request, response);
                break;
            }
            case "logout": {
                response.setContentType("text/html;charset=UTF-8");
                logout(request);
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                break;
            }
            case "register": {
                registerUser(dbBean, request, response);
            }
            //============================================================================================
            //MEMBER ACTIONS =============================================================================
            //
            case "dashboard": {
                request.getRequestDispatcher("/WEB-INF/userDashboard.jsp").forward(request, response);
                break;
            }
            case "makeclaim": {
                request.getRequestDispatcher("/WEB-INF/makeClaim.jsp").forward(request, response);
                break;
            }
            case "makepayment": {
                loadChargesForUser(dbBean, request);
                attachPaymentTypes(request);
                request.getRequestDispatcher("/WEB-INF/makePayment.jsp").forward(request, response);
                break;
            }
            case "paymenthistory": {
                loadAllUserPayments(dbBean, request);
                request.getRequestDispatcher("/WEB-INF/paymentHistory.jsp").forward(request, response);
                break;
            }
            case "claimhistory": {
                loadUserClaim(dbBean, request);
                request.getRequestDispatcher("/WEB-INF/claimHistory.jsp").forward(request, response);
                break;
            }
            case "insertclaim": {
                insertClaim(dbBean, request);
                request.getRequestDispatcher("/WEB-INF/makeClaimConf.jsp").forward(request, response);
                break;
            }
            case "insertpayment": {
                insertPayment(dbBean, request);
                request.getRequestDispatcher("/WEB-INF/makePaymentConf.jsp").forward(request, response);
                break;
            }

            //============================================================================================
            //ADMIN ACTIONS ==============================================================================
            //
            case "admindashboard": {
                request.getRequestDispatcher("/WEB-INF/adminDashboard.jsp").forward(request, response);
                break;
            }
            case "adminmakecharge": {
                loadSimpleMembers(dbBean, request);
                attachChargeTypes(request);
                request.getRequestDispatcher("/WEB-INF/makeCharge.jsp").forward(request, response);
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
            case "adminupdateclaim": {
                updateClaim(dbBean, request);
                request.getRequestDispatcher("/WEB-INF/handleClaims.jsp").forward(request, response);
                break;
            }
            case "admininsertcharge": {
                insertCharge(dbBean, request);
                request.getRequestDispatcher("/WEB-INF/makeCharge.jsp").forward(request, response);
                break;
            }
            case "adminupdatecharge": {
                updateChargeStatus(dbBean, request);
                request.getRequestDispatcher("/WEB-INF/handleCharges.jsp").forward(request, response);
                break;
            }
            case "adminclaimhistory": {
                loadClaims(dbBean, request);
                request.getRequestDispatcher("/WEB-INF/adminClaimHistory.jsp").forward(request, response);
                break;
            }
            case "adminpaymenthistory": {
                loadPayments(dbBean, request);
                request.getRequestDispatcher("/WEB-INF/adminPaymentHistory.jsp").forward(request, response);
                break;
            }
            default: {
                request.getRequestDispatcher("/WEB-INF/conErr.jsp").forward(request, response);
                break;
            }

        }
    }

    //MEMBER - Adds a payment for the selected charge
    private void insertPayment(Jdbc dbBean, HttpServletRequest request) {

        HttpSession session = request.getSession();

        int userId = ((int) request.getSession().getAttribute("id"));
        int chargeId = Integer.valueOf(request.getParameter("chargeId"));
        String paymentType = (String) request.getParameter("paymenttype");

        Charge charge = dbBean.getCharge(chargeId);
        Payment payment = new Payment(userId, charge.getId(), charge.getAmount(), paymentType);

        Payment responsePayment = dbBean.insertPayment(payment);
        charge = dbBean.getCharge(chargeId);

        session.setAttribute("paymentamount", String.valueOf(responsePayment.getAmount()));
        session.setAttribute("paymenttype", String.valueOf(responsePayment.getPaymentType()));
        session.setAttribute("chargenote", charge.getNote());
        session.setAttribute("chargestatus", charge.getStatus());
        session.setAttribute("paymentid", String.valueOf(responsePayment.getId()));
        session.setAttribute("paymentmessage", "Payment added successfully!");
    }

    //MEMBER - Gets and puts all charges for this user into the request
    private void loadChargesForUser(Jdbc dbBean, HttpServletRequest request) {
        List<Charge> charges = dbBean.getUnpaidChargesForUser((int) request.getSession().getAttribute("id"));
        request.setAttribute("list", charges);
    }

    //ADMIN - Updates the claim with the given status
    private void updateClaim(Jdbc dbBean, HttpServletRequest request) {
        int claimId = Integer.valueOf(request.getParameter("claimId"));
        ClaimStatus status = ClaimStatus.valueOf((String) request.getParameter("status"));

        dbBean.updateClaimStatus(claimId, status);

        int rowschanged = 1;
        request.setAttribute("rowschanged", String.valueOf(rowschanged));
        loadPendingClaims(dbBean, request);

    }

    //ADMIN - Inserts a new charge
    private void insertCharge(Jdbc dbBean, HttpServletRequest request) {
        int userId = Integer.valueOf(request.getParameter("member"));
        float amount = Float.valueOf(request.getParameter("amount"));
        String note = (String) request.getParameter("chargetype");

        dbBean.insertCharge(userId, amount, note);
        loadSimpleMembers(dbBean, request);
        attachChargeTypes(request);
        request.setAttribute("rowschanged", String.valueOf(1));
    }

    //ADMIN - Updates the status of the selected charges
    private void updateChargeStatus(Jdbc dbBean, HttpServletRequest request) {
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

    }

    //Gets and puts all payments for this user into the request
    private void loadAllUserPayments(Jdbc dbBean, HttpServletRequest request) {
        List<Payment> payments = dbBean.getAllPaymentsForUser((int) request.getSession().getAttribute("id"));
        request.setAttribute("list", payments);
        request.setAttribute("listcount", payments.size());
    }

    //Gets and puts all claims for this user into the request
    private void loadUserClaim(Jdbc dbBean, HttpServletRequest request) {
        List<Claim> claims = dbBean.getClaimsForUser((int) request.getSession().getAttribute("id"));
        request.setAttribute("list", claims);
        request.setAttribute("listcount", claims.size());
    }

    //ADMIN - Gets and puts all claims into the request
    private void loadClaims(Jdbc dbBean, HttpServletRequest request) {
        List<Claim> claims = dbBean.getClaims();
        request.setAttribute("list", claims);
        request.setAttribute("listcount", claims.size());
    }
    
       //ADMIN - Gets and puts all payments into the request
    private void loadPayments(Jdbc dbBean, HttpServletRequest request) {
        List<Payment> payments = dbBean.getPayments();
        request.setAttribute("list", payments);
        request.setAttribute("listcount", payments.size());
    }

    //ADMIN - Gets user ID and name and puts them into the request
    private void loadSimpleMembers(Jdbc dbBean, HttpServletRequest request) {
        List<BaseMember> members = dbBean.getMembers();
        request.setAttribute("list", members);
        if (members != null) {
            request.setAttribute("listcount", members.size());
        } else {
            request.setAttribute("listcount", 0);
        }
    }

//ADMIN - Puts the ChargeType enum into the request
    private void attachChargeTypes(HttpServletRequest request) {
        ChargeType[] ct = ChargeType.values();
        String[] cts = new String[ct.length];
        for (int i = 0; i < cts.length; i++) {
            cts[i] = ct[i].toString();
        }
        request.setAttribute("chargelist", cts);
    }

    private void attachPaymentTypes(HttpServletRequest request) {
        PaymentType[] pt = PaymentType.values();
        String[] cts = new String[pt.length];
        for (int i = 0; i < cts.length; i++) {
            cts[i] = pt[i].toString();
        }
        request.setAttribute("paymentlist", cts);
    }

    //ADMIN - Loads all charges that are pending and puts them in the request
    private void loadPendingCharges(Jdbc dbBean, HttpServletRequest request) {
        List<Charge> charges = dbBean.getChargesWhereStatus(ChargeStatus.PENDING);
        request.setAttribute("list", charges);
        if (charges != null) {
            request.setAttribute("listcount", charges.size());
        } else {
            request.setAttribute("listcount", 0);
        }
    }

    //ADMIN - Loads all claims that are pending and puts them in the request
    private void loadPendingClaims(Jdbc dbBean, HttpServletRequest request) {
        List<AdminClaim> claims = dbBean.getClaimsWhereStatus(ClaimStatus.PENDING);
        request.setAttribute("list", claims);
        if (claims != null) {
            request.setAttribute("listcount", String.valueOf(claims.size()));
        } else {
            request.setAttribute("listcount", String.valueOf(0));
        }
    }

    //ADMIN - Updates user status after admin has changed the status of charges
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

    //Inserts a new claim
    private void insertClaim(Jdbc dbBean, HttpServletRequest request) {

        int userId = ((int) request.getSession().getAttribute("id"));
        float amount = Float.valueOf((String) request.getParameter("amount"));
        java.sql.Date date = java.sql.Date.valueOf((String) request.getParameter("date"));
        String rationale = (String) request.getParameter(((String) "rationale").trim());
        Claim claim = new Claim(userId, amount, date, rationale);

        ClaimChecker cc = new ClaimChecker(dbBean, userId, claim);
        ClaimResponse cr = cc.isValid();

        Claim responseClaim = null;
        if (!cr.isValid()) {
            claim.setStatus("DECLINED");
        }
        responseClaim = dbBean.insertClaim(claim);

        request.getSession().setAttribute("claimamount", String.valueOf(responseClaim.getAmount()));
        request.getSession().setAttribute("claimid", String.valueOf(responseClaim.getId()));
        request.getSession().setAttribute("claimstatus", String.valueOf(responseClaim.getStatus()));
        request.getSession().setAttribute("claimmessage", cr.getReason());

    }

    //Logs the user out
    private void logout(HttpServletRequest request) {
        request.getSession().invalidate();
        request.setAttribute("message", "You were successfully logged out.");
    }

    //Logs the user in
    private void login(HttpServletRequest request, HttpServletResponse response, Connection connection) {
        HttpSession session = request.getSession();
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
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void registerUser(Jdbc dbBean, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String[] query = new String[4];
        query[0] = (String) request.getParameter("name");
        query[1] = (String) request.getParameter("address");
        query[2] = (String) request.getParameter("dob");
        query[3] = (String) request.getParameter("registrationdate");

        Jdbc jdbc = (Jdbc) session.getAttribute("dbbean");
        if (jdbc == null) {
            jdbc = new Jdbc();
            session.setAttribute("dbbean", jdbc);
        }
        Connection connection = (Connection) request.getServletContext().getAttribute("connection");
        jdbc.connect(connection);

        try {
            if (jdbc == null) {
                request.getRequestDispatcher("/WEB-INF/conErr.jsp").forward(request, response);
            }

            for (String q : query) {
                if (q.length() == 0) {
                    request.setAttribute("registrationState", "true");
                    request.setAttribute("message", "Fields cannot be empty");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                }
            }
            if (query[0] == null) {
                request.setAttribute("registrationState", "true");
                request.setAttribute("message", "Name cannot be empty");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            } else {
                String[] userDetails = jdbc.insertUser(query);
                if (userDetails != null) {
                    request.setAttribute("username", userDetails[0]);
                    request.setAttribute("password", userDetails[1]);
                    request.getRequestDispatcher("/WEB-INF/userRegConf.jsp").forward(request, response);
                } else {
                    request.setAttribute("registrationState", "true");
                    request.setAttribute("message", "An error occured whilst creating user, please try register again.");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
