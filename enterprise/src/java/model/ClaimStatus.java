/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Vilde
 */
public enum ClaimStatus {
    DECLINED ("DECLINED"), APPROVED ("APPROVED"), PENDING ("PENDING");
    
    private final String name;  
    private ClaimStatus(String s) {
        this.name = s;
    }
    
    @Override
    public String toString() {
        return this.name();
    }
}
