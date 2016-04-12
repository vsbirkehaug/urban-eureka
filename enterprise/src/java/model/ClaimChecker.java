/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vilde
 */
public class ClaimChecker {

    private final Claim claim;
    private final HttpSession session;
    
    public ClaimChecker(HttpSession session, Claim claim) {
          this.session = session;
          this.claim = claim;
    }
    
    public ClaimResponse isValid() {  
        
        int claimsThisYear = ((Jdbc)session.getAttribute("dbbean")).getApprovedClaimsLast12Months((int)session.getAttribute("id"));
        java.sql.Timestamp dor = ((Jdbc)session.getAttribute("dbbean")).getDOR((int)session.getAttribute("id"));
        if(claimsThisYear >= 2) {
            return new ClaimResponse(false, "You have reached the claim limit(2) for this year. Your claim was automatically rejected.");
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
