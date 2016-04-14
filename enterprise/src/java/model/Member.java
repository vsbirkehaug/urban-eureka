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
public class Member extends BaseMember {
    
    protected String password;
    protected String address;
    protected Date dob;
    protected Date dor;
    protected String status;
    protected String username;

    public Member(int id, String username, String password, String name, String address, Date dob, Date dor, String status) {
        super(id, name);
        this.password = password;
        this.username = username;
        this.address = address;
        this.dob = dob;
        this.dor = dor;
        this.status = status;
    }
    
        public Member(int id, String username, String name, String address, Date dob, Date dor, String status) {
        super(id, name);
        this.username = username;
        this.address = address;
        this.dob = dob;
        this.dor = dor;
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getDor() {
        return dor;
    }

    public void setDor(Date dor) {
        this.dor = dor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }        
}
