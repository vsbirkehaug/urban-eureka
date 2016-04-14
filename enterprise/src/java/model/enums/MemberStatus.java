/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.enums;

/**
 *
 * @author Vilde
 */
public enum MemberStatus {
    REGISTERED("REGISTERED"), ACTIVE("ACTIVE"), SUSPENDED("SUSPENDED");
    
    private final String name;  
    private MemberStatus(String s) {
        this.name = s;
    }
    
    @Override
    public String toString() {
        return this.name();
    }
    
}
