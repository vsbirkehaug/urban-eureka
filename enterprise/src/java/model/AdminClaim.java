/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Date;

/**
 *
 * @author Vilde
 */
public class AdminClaim extends Claim{
    
    private int userPendingClaims;
    private int userDeclinedClaims;

    public int getUserPendingClaims() {
        return userPendingClaims;
    }

    public int getUserDeclinedClaims() {
        return userDeclinedClaims;
    }

    public int getUserApprovedClaims() {
        return userApprovedClaims;
    }
    private int userApprovedClaims;
        
    public AdminClaim(int id, String userId, float amount, Date date, String rationale, String status, int userPendingClaims, int userDeclinedClaims, int userApprovedClaims) {
     super(id, userId, amount, date, rationale, status);
        this.userPendingClaims = userPendingClaims;
        this.userDeclinedClaims = userDeclinedClaims;
        this.userApprovedClaims = userApprovedClaims;
    }
    
}
