/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.Calendar;
import model.Claim;
import model.ClaimResponse;
import controllers.Jdbc;

/**
 *
 * @author Vilde
 */
public class ClaimChecker {

    private final Claim claim;
    private final Jdbc jdbc;
    private final int userId;
    private final int claimsAllowedPerYear = 2;
    
    public ClaimChecker(Jdbc jdbc, int userId, Claim claim) {
          this.jdbc = jdbc;
          this.claim = claim;
          this.userId = userId;
    }
    
    public ClaimResponse isValid() {  
        
        int approvedClaimsThisYear = jdbc.getApprovedClaimsYearOfDate(userId, claim.getDate());
        java.sql.Timestamp dor =jdbc.getDOR(userId);
        
        if(approvedClaimsThisYear >= claimsAllowedPerYear) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new java.util.Date(claim.getDate().getTime()));         
            return new ClaimResponse(false, ("You have reached the claim limit(2) for the year " + cal.get(Calendar.YEAR)) + ". Your claim was automatically rejected.");
        } else if (approvedClaimsThisYear < 0){
            return new ClaimResponse(true, "The system has encountered an error, but your claim has been registered and will be handled by an administrator.");    //LETS THE ADMIN HANDLE THIS ERROR
        } else if (differenceInMonths(dor, claim.getDate())<6){
            return new ClaimResponse(false, "You cannot make a claim untill 6 months after registration. Your claim was automatically rejected.");    
        } else {
            return new ClaimResponse(true, "Your claim has been registered and is pending approval by an administrator.");    
        }              
    }
    
   //Source: http://stackoverflow.com/questions/13566630/number-of-months-between-2-java-util-date-excluding-day-of-month
    private static int differenceInMonths(java.sql.Timestamp d1, java.sql.Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(new java.util.Date(d1.getTime()));
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new java.util.Date(d2.getTime()));
        int diff = 0;
        if (c2.after(c1)) {
            while (c2.after(c1)) {
                c1.add(Calendar.MONTH, 1);
                if (c2.after(c1)) {
                    diff++;
                }
            }
        } else if (c2.before(c1)) {
            while (c2.before(c1)) {
                c1.add(Calendar.MONTH, -1);
                if (c1.before(c2)) {
                    diff--;
                }
            }
        }
        return diff;
    }
}
