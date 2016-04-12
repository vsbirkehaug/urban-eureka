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
public class Claim {
    
    private int id;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public float getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String getRationale() {
        return rationale;
    }

    public String getStatus() {
        return status;
    }
    private int userId;
    private float amount;
    private java.sql.Date date;
    private String rationale;
    private String status;
    
    public Claim(int userId, float amount, java.sql.Date date, String rationale) {
        this.userId = userId;
        this.amount = amount;
        this.date = date;
        this.rationale = rationale;
        this.id = -1;
        this.status = "";      
    }
    
    public Claim(int id, int userId, float amount, java.sql.Date date, String rationale, String status) {
        this.userId = userId;
        this.amount = amount;
        this.date = date;
        this.rationale = rationale;
        this.id = id;
        this.status = status;        
    }   

    public void setStatus(String status) {
        this.status = status;
    }
    
}
