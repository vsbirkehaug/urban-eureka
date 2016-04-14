/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import static java.sql.Types.NULL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Claim;
import controllers.Register;
import java.util.List;
import model.AdminClaim;
import model.Charge;
import model.ChargeStatus;
import model.ClaimStatus;
import model.Member;
import model.MemberStatus;
import model.Payment;
import model.SimpleMember;

/**
 *
 * @author me-aydin, vilde
 */
public class Jdbc {

    Connection connection = null;
    Statement statement = null;
    ResultSet rs = null;

    public Jdbc() {
    }

    public void connect(Connection con) {
        connection = con;
    }

    private ArrayList rsToList() throws SQLException {
        ArrayList aList = new ArrayList();

        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            String[] s = new String[cols];
            for (int i = 1; i <= cols; i++) {
                s[i - 1] = rs.getString(i);
            }
            aList.add(s);
        } // while    
        return aList;
    } //rsToList

    private String makeTable(ArrayList list) {
        StringBuilder b = new StringBuilder();
        String[] row;
        b.append("<table border=\"3\">");
        for (Object s : list) {
            b.append("<tr>");
            row = (String[]) s;
            for (String row1 : row) {
                b.append("<td>");
                b.append(row1);
                b.append("</td>");
            }
            b.append("</tr>\n");
        } // for
        b.append("</table>");
        return b.toString();
    }//makeHtmlTable

    private void select(String query) {
        //Statement statement = null;

        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            //statement.close();
        } catch (SQLException e) {
            System.out.println("way way" + e);
            //results = e.toString();
        }
    }

    public String retrieve(String query) throws SQLException {
        String results = "";
        select(query);
//        try {
//            
//            if (rs==null)
//                System.out.println("rs is null");
//            else
//                results = makeTable(rsToList());
//        } catch (SQLException ex) {
//            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return makeTable(rsToList());//results;
    }

    public boolean exists(String user) {
        boolean exists = false;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT (`username`) FROM members WHERE `username` = ? ");
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exists = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exists;
    }

    public int usernameExists(String user) {
        int counter = 0;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(`username`) FROM members WHERE `username` LIKE ?");
            ps.setString(1, user + '%');
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                counter = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return counter;
    }

    private String getUsernameFromName(String name) {
        String[] names = name.split(" ");
        String username = "";

        for (int i = 0; i < names.length; i++) {
            if (i == names.length - 1) {
                username += "-" + names[i];
            } else {
                username += names[i].substring(0, 1);
            }
        }

        int existingUsers = usernameExists(username);
        while (exists(username + String.valueOf(existingUsers))) {
            existingUsers++;
        }
        if (existingUsers > 0) {
            username = username + String.valueOf(existingUsers);
        }

        return username;
    }

    private String getPasswordFromDob(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date parsed = format.parse(date);
            SimpleDateFormat pwformat = new SimpleDateFormat("ddMMyy");

            return pwformat.format(parsed);

        } catch (ParseException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String[] insertUser(String[] str) {
        PreparedStatement ps = null;
        String username = getUsernameFromName(str[0].trim().toLowerCase());
        String name = str[0].trim();
        String password = getPasswordFromDob(str[2]);
        String address = str[1].trim();
        java.sql.Date dob = java.sql.Date.valueOf(str[2]);
        java.sql.Date dor = java.sql.Date.valueOf(str[3]);

        int result = -1;
        try {
            ps = connection.prepareStatement("INSERT INTO members (`username`, `name`, `password`, `address`, `dob`, `dor`) VALUES (?,?,?,?,?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, name);
            ps.setString(3, password);
            ps.setString(4, address);
            ps.setDate(5, dob);
            ps.setDate(6, dor);
            ps.executeUpdate();
            ResultSet key = ps.getGeneratedKeys();
            if (key != null) {
                key.next();
                result = key.getInt(1);

                insertMembershipFeeCharge(result);
            } else {
                return null;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new String[]{username, password};
    }

    private boolean insertMembershipFeeCharge(int userId) {
        PreparedStatement ps = null;

        int result = -1;
        try {
            ps = connection.prepareStatement("INSERT INTO charges (`user_id`, `amount`, `status`, `note`) VALUES (?,?,?,?)", Statement.SUCCESS_NO_INFO);
            ps.setInt(1, userId);
            ps.setFloat(2, 7.0f);
            ps.setString(3, ChargeStatus.DUE.toString());
            ps.setString(4, "REGISTRATION FEE");
            result = ps.executeUpdate();
            ps.close();

            if (result == 1) {
                System.out.println("1 row added.");
            } else {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public Claim insertClaim(Claim claim) {
        PreparedStatement ps = null;

        int member_id = claim.getUserId();
        float amount = claim.getAmount();
        java.sql.Date date = claim.getDate();
        String rationale = claim.getRationale();
        Claim newClaim = null;
        String status = null;
        if (claim.getStatus().length() > 0) {
            status = claim.getStatus();
        }

        try {
            if (status != null) {
                ps = connection.prepareStatement("INSERT INTO claims (`mem_id`, `amount`, `date`, `rationale`, `status`) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(5, status);
            } else {
                ps = connection.prepareStatement("INSERT INTO claims (`mem_id`, `amount`, `date`, `rationale`) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            }
            ps.setInt(1, member_id);
            ps.setFloat(2, amount);
            ps.setDate(3, date);
            ps.setString(4, rationale);
            ps.executeUpdate();
            ResultSet key = ps.getGeneratedKeys();
            key.next();
            int result = key.getInt(1);
            ps.close();

            ps = connection.prepareStatement("SELECT * FROM claims WHERE id = ?");
            ps.setInt(1, result);
            ResultSet rs = ps.executeQuery();
            rs.next();
            newClaim = new Claim(rs.getInt("id"), rs.getInt("mem_id"), rs.getFloat("amount"), rs.getDate("date"), rs.getString("rationale"), rs.getString("status"));

        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return newClaim;
    }

    public Payment insertPayment(Payment payment) {
        PreparedStatement ps = null;

        int member_id = payment.getUserId();
        int charge_id = payment.getChargeId();
        float amount = payment.getAmount();
        java.sql.Timestamp date = payment.getDate();

        Payment responsePayment = null;

        try {
            //insert payment
            ps = connection.prepareStatement("INSERT INTO payments (`mem_id`, `charge_id`, `amount`, `payment_type`, `date`) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, member_id);
            ps.setInt(2, charge_id);
            ps.setFloat(3, amount);
            ps.setString(4, payment.getPaymentType());
            ps.setTimestamp(5, date);
            ps.executeUpdate();

            ResultSet key = ps.getGeneratedKeys();
            key.next();
            int result = key.getInt(1);
            ps.close();

            updateChargeStatus(charge_id, ChargeStatus.PENDING);

            //get inserted payment        
            ps = connection.prepareStatement("SELECT * FROM payments WHERE id = ?");
            ps.setInt(1, result);
            ResultSet rs = ps.executeQuery();
            rs.next();
            responsePayment = new Payment(rs.getInt("id"), rs.getInt("mem_id"), rs.getInt("charge_id"), rs.getFloat("amount"), rs.getString("payment_type"), rs.getTimestamp("date"));

        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return responsePayment;
    }

    public List<Charge> getUnpaidChargesForUser(int userId) {

        List<Charge> resultList = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM charges WHERE user_id = ? AND status = ? OR status = ?");
            ps.setInt(1, userId);
            ps.setString(2, ChargeStatus.DUE.toString());
            ps.setString(2, ChargeStatus.DECLINED.toString());
            ResultSet rs = ps.executeQuery();
            resultList = new ArrayList<>();
            while (rs.next()) {
                resultList.add(new Charge(rs.getInt("id"), rs.getInt("user_id"), rs.getFloat("amount"), rs.getString("status"), rs.getString("note")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultList;
    }

    public List<Charge> getAllChargesWhereStatus(ChargeStatus status) {

        List<Charge> resultList = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT charges.id,members.name,charges.amount, payments.id as payment_id, payments.payment_type, payments.date as payment_date, charges.status,charges.note FROM charges JOIN members ON charges.user_id = members.id JOIN payments on charges.id = payments.charge_id WHERE charges.status = ?");
            ps.setString(1, status.toString());
            ResultSet rs = ps.executeQuery();
            resultList = new ArrayList<>();
            while (rs.next()) {
                resultList.add(new Charge(rs.getInt("id"), rs.getString("name"), rs.getInt("payment_id"), rs.getString("payment_type"), rs.getTimestamp("payment_date"), rs.getFloat("amount"), rs.getString("status"), rs.getString("note")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultList;
    }

    public List<AdminClaim> getAllClaimsWhereStatus(ClaimStatus status) {

//SELECT claims.id, members.name, claims.amount, claims.date as record_date, claims.rationale, claims.status, 
//(SELECT COUNT(*) FROM claims c WHERE claims.mem_id = 8 AND claims.status = "PENDING" AND YEAR(c.date) = YEAR(record_date)) as userPendingClaims,
//(SELECT COUNT(*) FROM claims c WHERE claims.mem_id = 8 AND claims.status = "DECLINED" AND YEAR(c.date) = YEAR(record_date)) as userDeclinedClaims,
//(SELECT COUNT(*) FROM claims c WHERE claims.mem_id = 8 AND claims.status = "APPROVED" AND YEAR(c.date) = YEAR(record_date)) as userApprovedClaims
//FROM claims JOIN members ON claims.mem_id = members.id WHERE claims.status = "PENDING"
        List<AdminClaim> resultList = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT claims.id, members.name, members.id, claims.amount, claims.date as record_date, claims.rationale, claims.status, "
                    + " (SELECT COUNT(*) FROM claims c WHERE c.mem_id = members.id AND c.status = ? AND YEAR(c.date) = YEAR(record_date)) as userPendingClaims,"
                    + " (SELECT COUNT(*) FROM claims c WHERE c.mem_id = members.id AND c.status = ? AND YEAR(c.date) = YEAR(record_date)) as userDeclinedClaims,"
                    + " (SELECT COUNT(*) FROM claims c WHERE c.mem_id = members.id AND c.status = ? AND YEAR(c.date) = YEAR(record_date)) as userApprovedClaims"
                    + " FROM claims JOIN members ON claims.mem_id = members.id WHERE claims.status = ?");
            ps.setString(1, ClaimStatus.PENDING.toString());
            ps.setString(2, ClaimStatus.DECLINED.toString());
            ps.setString(3, ClaimStatus.APPROVED.toString());
            ps.setString(4, status.toString());
            ResultSet rs = ps.executeQuery();
            resultList = new ArrayList<>();
            while (rs.next()) {
                resultList.add(new AdminClaim(rs.getInt("id"), rs.getString("name"), rs.getFloat("amount"), rs.getDate("record_date"), rs.getString("rationale"), rs.getString("status"), rs.getInt("userPendingClaims"), rs.getInt("userDeclinedClaims"), rs.getInt("userApprovedClaims")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultList;
    }

    public List<Payment> getAllPaymentsForUser(int userId) {

        List<Payment> resultList = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT payments.id,payments.mem_id,payments.charge_id,payments.payment_type,payments.amount,payments.date,charges.note,charges.status FROM payments JOIN charges ON payments.charge_id = charges.id WHERE mem_id = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            resultList = new ArrayList<>();
            while (rs.next()) {
                resultList.add(new Payment(rs.getInt("id"), rs.getInt("mem_id"), rs.getInt("charge_id"), rs.getFloat("amount"), rs.getString("payment_type"), rs.getTimestamp("date"), rs.getString("note"), rs.getString("status")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultList;
    }

    public List<Claim> getAllClaimsForUser(int userId) {

        List<Claim> resultList = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM claims WHERE mem_id = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            resultList = new ArrayList<>();
            while (rs.next()) {
                resultList.add(new Claim(rs.getInt("id"), rs.getInt("mem_id"), rs.getFloat("amount"), rs.getDate("date"), rs.getString("rationale"), rs.getString("status")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultList;
    }

    public Charge getCharge(int chargeId) {

        Charge result = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM charges WHERE id = ?");
            ps.setInt(1, chargeId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            result = new Charge(rs.getInt("id"), rs.getInt("user_id"), rs.getFloat("amount"), rs.getString("status"), rs.getString("note"));

        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public int getApprovedClaimsLast12Months(int memberId) {
        PreparedStatement ps = null;
        int result = -1;

        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        calendar.add(Calendar.YEAR, -1);
        java.util.Date oneYearAgo = calendar.getTime();
        java.sql.Timestamp aYearAgo = new java.sql.Timestamp(oneYearAgo.getTime());

        try {
            ps = connection.prepareStatement("SELECT COUNT(`mem_id`) FROM claims WHERE `mem_id` = ? AND `date` WHERE (`date` BETWEEN ? AND ?) AND WHERE `status' = ?");
            ps.setInt(1, memberId);
            ps.setTimestamp(2, aYearAgo);
            ps.setTimestamp(3, currentTimestamp);
            ps.setString(4, ClaimStatus.APPROVED.toString());
            ResultSet rs = ps.executeQuery();
            rs.next();
            result = rs.getInt(1);
            rs.close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public int getApprovedClaimsYearOfDate(int memberId, java.sql.Date claimdate) {
        PreparedStatement ps = null;
        int result = -1;

        Calendar cal = Calendar.getInstance();
        cal.setTime(claimdate);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        java.sql.Date start = new java.sql.Date(cal.getTimeInMillis());
        cal.set(Calendar.MONTH, 11); // 11 = december
        cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve
        java.sql.Date end = new java.sql.Date(cal.getTimeInMillis());

        try {
            ps = connection.prepareStatement("SELECT COUNT(`mem_id`) FROM claims WHERE `mem_id` = ? AND (`date` BETWEEN ? AND ?) AND `status` = ?");
            ps.setInt(1, memberId);
            ps.setDate(2, start);
            ps.setDate(3, end);
            ps.setString(4, ClaimStatus.APPROVED.toString());
            ResultSet rs = ps.executeQuery();
            rs.next();
            result = rs.getInt(1);
            rs.close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public java.sql.Timestamp getDOR(int userId) {
        PreparedStatement ps = null;
        java.sql.Timestamp dor;
        try {
            ps = connection.prepareStatement("SELECT `dor` FROM members WHERE id = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            dor = rs.getTimestamp("dor");
            return dor;
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void update(String[] str) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("Update members Set password=? where username=?", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, str[1].trim());
            ps.setString(2, str[0].trim());
            ps.executeUpdate();

            ps.close();
            System.out.println("1 rows updated.");
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(String user) {

        String query = "DELETE FROM members "
                + "WHERE username = '" + user.trim() + "'";

        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("way way" + e);
            //results = e.toString();
        }
    }

    public void closeAll() {
        try {
            rs.close();
            statement.close();
            //connection.close();                                         
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    void updateChargeStatus(int chargeId, ChargeStatus status) {
        PreparedStatement ps;
        try {

            ps = connection.prepareStatement("UPDATE charges SET status = ? WHERE id = ?");
            ps.setString(1, status.toString());
            ps.setInt(2, chargeId);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void updateClaimStatus(int claimId, ClaimStatus status) {
        PreparedStatement ps;
        try {

            ps = connection.prepareStatement("UPDATE claims SET status = ? WHERE id = ?");
            ps.setString(1, status.toString());
            ps.setInt(2, claimId);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void updateMemberStatus(int userId, MemberStatus status) {
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement("UPDATE members SET status = ? WHERE id = ?");
            ps.setString(1, status.toString());
            ps.setInt(2, userId);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    int getDueOrDeclinedChargeCount(int userId) {
        int count = -1;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(id) FROM charges WHERE user_id = ? AND status != ?");
            ps.setInt(1, userId);
            ps.setString(2, ChargeStatus.APPROVED.toString());
            ResultSet rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return count;
    }

    public List<SimpleMember> getMembers() {
        PreparedStatement ps = null;
        List<SimpleMember> resultList = null;
        try {
            ps = connection.prepareStatement("SELECT id, name FROM members");
            ResultSet rs = ps.executeQuery();
            resultList = new ArrayList<>();
            while (rs.next()) {
                resultList.add(new SimpleMember(rs.getInt("id"), rs.getString("name")));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultList;
    }

    void insertCharge(int userId, float amount, String note) {
        PreparedStatement ps = null;

        try {
            //insert charge
            ps = connection.prepareStatement("INSERT INTO charges (`user_id`, `amount`, `note`, `status`) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            ps.setFloat(2, amount);
            ps.setString(3, note);
            ps.setString(4, ChargeStatus.DUE.toString());
            ps.executeUpdate();

            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Claim> getAllClaims() {
        List<Claim> resultList = null;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("SELECT * FROM claims");
            ResultSet rs = ps.executeQuery();
            resultList = new ArrayList<>();
            while (rs.next()) {
                resultList.add(new Claim(rs.getInt("id"), rs.getInt("mem_id"), rs.getFloat("amount"), rs.getDate("date"), rs.getString("rationale"), rs.getString("status")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultList;
    }

}
